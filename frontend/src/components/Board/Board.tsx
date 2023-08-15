// Import CSS
import './Board.scss'

// Import components
import Piece from '../Piece/Piece'
import Promotion from '../Promotion/Promotion'

// Import utils
import { convertSquare } from '../../utils/convertSquare'

// Import libraries
import { useState, useEffect } from 'react'

// @ts-ignore
import useSound from 'use-sound';
import moveSfx from '../../assets/move-self.mp3';
import captureSfx from '../../assets/capture.mp3';
import beepSfx from '../../assets/lichess-beep.mp3';
import axios, { AxiosResponse } from 'axios'

// Trivia

import SwitchButton from '../../assets/switch.png'
import backendUrl from '../../../config'

interface Square {
    [key: string]: { 'pieceType': string, 'pieceColor': string, 'availableMovements': string[], 'isChecked': boolean };
}

function Board() {

    /**
     * The promotion type piece required
     */
    var promotionPiece: string = "_"

    // States

    /// Visualisation
    const [board, setBoard] = useState<Square>({})
    const [sideView, selectSideView] = useState<boolean>(false)
    const [rowOrder, selectRowOrder] = useState<number[]>([8, 7, 6, 5, 4, 3, 2, 1])
    const [columnOrder, selectcolumnOrder] = useState<number[]>([1, 2, 3, 4, 5, 6, 7, 8])

    /// Move
    const [selectedPiece, selectSelectedPiece] = useState<string>('')

    /// Promotion
    const [colorPromotion, setColorPromotion] = useState<string>("w")
    const [targetSquarePromotion, selectTargetSquarePromotion] = useState<string>("")

    /// Game 
    const [winner, setWinner] = useState<string>("")
    const [turn, setTurn] = useState<string>("")

    /// Sounds
    const [playMove] = useSound(moveSfx);
    const [playCapture] = useSound(captureSfx);
    const [playBeep] = useSound(beepSfx);

    /**
     * When the board is updated, update the square for the 
     * potential checked pieces by :
     * - first remove all the "checked" class to all the elements
     * - add the "checked" class to the squares with a checked piece (kings)
    */
    useEffect(() => {

        // Remove "checked" to all squares
        for (const element of document.querySelectorAll(".checked")) {
            element.classList.remove("checked")
        };

        // Add "checked" to the relevant squares
        let k: keyof typeof board;
        for (k in board) {

            if (board[k].isChecked) {

                document.getElementById(k)?.classList.add("checked")
            }
        }

    }, [board])

    /**
     * When a winner is declared, play the sound
     */
    useEffect(() => {
        playBeep()
    }, [winner])

    /**
     * Change the side view accordingly
     */
    useEffect(() => {

        selectRowOrder([...rowOrder.reverse()])
        selectcolumnOrder([...columnOrder.reverse()])

    }, [sideView])


    /**
     * When a piece is selected, update the possible movements squares
     */
    useEffect(() => {
        showMovements(selectedPiece);

    }, [selectedPiece])

    /**
     * Update board information with axios backend response.
     * @param res The axios response from the backend.
     */
    function updateBoardData(res: AxiosResponse) {
        setBoard(res.data.board)
        setTurn(res.data.turn)
        setWinner(res.data.winner)
    }

    /**
     * When loading the page, fetch the board.
     * @todo make this request every 0.5 seconds for updates.
     */
    useEffect(() => {
        axios
            .get(backendUrl + 'board')
            .then((res) => {
                updateBoardData(res)
            })
    }, [])

    /**
     * Initialize the board from the backend.
     */
    const initializeBoard = () => {
        axios
            .get(backendUrl + 'initialize')
            .then((res) => {
                updateBoardData(res)
                playBeep()
            })
    }




    /**
     * Move a piece by calling the backend.
     * @param selectedPiece The square with the piece we want to move.
     * @param targetSquare The square we want to move the piece.
     */
    const movePieceBackend = (selectedPiece: String, targetSquare: String) => {

        axios({
            method: 'get',
            url: backendUrl + 'play',
            params: { "selectedSquare": selectedPiece, "targetSquare": targetSquare, "promotion": promotionPiece },

        }).then((res) => {
            updateBoardData(res)
        })
    }

    /** Click event handler.
     *
     * @param e 
     */
    const handleClick = async (e: React.MouseEvent) => {

        const eventType = e.type
        const target = e.currentTarget
        const componentId = e.currentTarget.id

        if (eventType == 'click') {

            document.getElementById('modal')!.style.visibility = 'hidden'
            if (target.classList.contains('dark') || target.classList.contains('light')) {

                const childDiv: Element = target.children[0]
                const classList: DOMTokenList = childDiv.classList

                if (classList.contains('move')) {
                    movePiece(componentId)
                } else {
                    selectSelectedPiece(componentId)
                }
            }
        }
    }

    /** 
     * Show the available movements of the selected piece.
     * 
     * If selected square is a key of the board Object, add the class 'move' to every
     * piece/empty classes.
     * 
     * @param selectedSquare The selected square.
     */
    function showMovements(selectedSquare: string) {

        cleanMovements()

        Object.keys(board).indexOf(selectedSquare) > -1 ?
            handleMovements(selectedSquare) : null

    }

    /** 
     * Add markers on available movements and highlight selected square.
     * 
     * @param selectedSquare The selected square.
     */
    function handleMovements(selectedSquare: string) {

        document.getElementById(selectedSquare)?.classList.add("selected")
        board[selectedSquare].availableMovements.map((id) => {

            let child = document.getElementById(id)?.children[0]
            child?.classList.add('move')

        })
    }

    /**
     * Remove every 'move' class from every div containing it.
     */
    function cleanMovements() {

        for (const element of document.querySelectorAll(".move")) {
            element.classList.remove("move")
        };

        for (const element of document.querySelectorAll(".selected")) {
            element.classList.remove("selected")
        };
    }


    /**
     * Move a piece on the board.
     * Replace the targetSquare key entry of board with the selectedSquare key entry.
     * Remove the selectedSquare key entry afterwards to clean the square.
     * 
     * @param targetSquare The targetted square.
     */
    function movePiece(targetSquare: string) {

        console.log(targetSquare)
        console.log(promotionPiece)


        if (board[selectedPiece].pieceColor == turn) {

            if (board[selectedPiece].pieceType == "Pawn" && ((board[selectedPiece].pieceColor == "w" && targetSquare[1] == "8") // Promotion
                || (board[selectedPiece].pieceColor == "b" && targetSquare[1] == "1"))) {
                doPromotion()
                selectTargetSquarePromotion(targetSquare)

            } else {
                Object.keys(board).indexOf(targetSquare) > -1 ? playCapture() : playMove()
                board[targetSquare] = board[selectedPiece]
                delete board[selectedPiece]
                setBoard({
                    ...board,

                });
                movePieceBackend(selectedPiece, targetSquare)
                selectSelectedPiece('')
            }


        }
    }

    /**
     * Initialize the promotion process.
     * Set the color of the promotions pieces to the color of the pawn that promotes.
     */
    const doPromotion = () => {
        setColorPromotion(board[selectedPiece].pieceColor)
        document.getElementById('modal')!.style.visibility = 'visible'

    }

    function updateBoardSync(name: string, color: string) {
        return new Promise((resolve) => {
            board[selectedPiece] = { 'pieceType': name, 'pieceColor': color, 'availableMovements': [], 'isChecked': false }
            resolve(0)
        })
    }

    /**
     * Set the current promotion to the one assigned,
     * and move the piece with its promote status by calling the backend.
     * @param name The name of the promotion.
     * @param color The color of the piece.
     */
    function selectPromotion(name: string, color: string) {
        promotionPiece = name[0]
        updateBoardSync(name, color).then(() => {
            document.getElementById('modal')!.style.visibility = 'hidden'
            movePiece(targetSquarePromotion)
        }
        )
    }

    return (
        <>

            <div className="modal" id="modal">
                <div className="modal-back"></div>
                <div className="modal-container">
                    <Promotion color={colorPromotion} onInteraction={selectPromotion} />
                </div>
            </div>
            <table className='board'>

                <tbody>
                    {rowOrder.map((rowNumber) => {
                        return (

                            <tr className='row' key={rowNumber} id={rowNumber.toString()} >
                                <p className='rowNumber'>{rowNumber}</p>
                                {
                                    columnOrder.map((columnNumber) => {
                                        let selectedSquare: string = convertSquare(rowNumber, columnNumber);

                                        return (
                                            <td key={selectedSquare} id={convertSquare(rowNumber, columnNumber)}
                                                className={(rowNumber + columnNumber) % 2 == 0 ? 'dark' : 'light'}
                                                onDrop={(e) => {

                                                    if (e.currentTarget.classList.contains('dark') || e.currentTarget.classList.contains('light')) {

                                                        const child = e.currentTarget.children[0]
                                                        if (child.classList.contains('move')) {
                                                            movePiece(e.currentTarget.id)
                                                        }

                                                    } else {
                                                        const target = e.target as Element
                                                        if (target.classList.contains('move')) {
                                                            movePiece(e.currentTarget.id)
                                                        }
                                                    }
                                                }}
                                                onDragOver={event => event.preventDefault()}
                                                onClick={(e) => handleClick(e)}

                                            >

                                                {Object.keys(board).indexOf(selectedSquare) > -1
                                                    ? <div className='piece'
                                                        draggable="true"
                                                        onDragStart={() => selectSelectedPiece(selectedSquare)}

                                                        onDragEnd={() => {

                                                            selectSelectedPiece('')
                                                        }}>

                                                        <Piece name={board[selectedSquare].pieceType} color={board[selectedSquare].pieceColor} /></div>

                                                    : <div className='empty'></div>}

                                            </td>)
                                    })
                                }

                            </tr>
                        )
                    }
                    )}
                    <tr className='row'>
                        {[''].concat(columnOrder.map(p => convertSquare(0, p)).map(p => p[0])).map((columnNumber) => {
                            return (
                                <td className='columnNumber'>
                                    {columnNumber}
                                </td>)
                        })}

                    </tr>
                </tbody>
            </table >
            <img onClick={() => selectSideView(!sideView)} src={SwitchButton} className='switchButton' width='30px' />
            <button onClick={initializeBoard}> Reset Board </button>
        </>
    )
}


export default Board
