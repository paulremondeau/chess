import './Board.scss'
import { useState, useEffect } from 'react'

import Piece from '../Piece/Piece'
import { convertSquare } from '../../utils/convertSquare'

// @ts-ignore
import useSound from 'use-sound';

import moveSfx from '../../assets/move-self.mp3';
import captureSfx from '../../assets/capture.mp3';
import beepSfx from '../../assets/lichess-beep.mp3';

interface Square {
    [key: string]: { 'pieceType': string, 'pieceColor': string, 'availableMovements': string[], 'isChecked': boolean };
}

import SwitchButton from '../../assets/switch.png'

import axios from 'axios'

import backendUrl from '../../../config'

import Promotion from '../Promotion/Promotion'




function Board() {

    const [colorPromotion, setColorPromotion] = useState<string>("w")
    const [promotionPiece, selectPromotionPiece] = useState<string>("_")
    const [targetSquarePromotion, selectTargetSquarePromotion] = useState<string>("")

    const [playMove] = useSound(moveSfx);
    const [playCapture] = useSound(captureSfx);
    const [playBeep] = useSound(beepSfx);

    const [board, setBoard] = useState<Square>({})
    const [view, selectView] = useState<boolean>(false)

    const [rowOrder, selectRowOrder] = useState<number[]>([8, 7, 6, 5, 4, 3, 2, 1])
    const [columnOrder, selectcolumnOrder] = useState<number[]>([1, 2, 3, 4, 5, 6, 7, 8])

    const [winner, setWinner] = useState<string>("")
    const [turn, setTurn] = useState<string>("")

    useEffect(() => {

        for (const element of document.querySelectorAll(".checked")) {
            element.classList.remove("checked")
        };

        let k: keyof typeof board;  // Type is "one" | "two" | "three"
        for (k in board) {

            if (board[k].isChecked) {

                document.getElementById(k)?.classList.add("checked")
            }
        }

    }, [board])

    useEffect(() => {
        playBeep()
    }, [winner])

    useEffect(() => {

        selectRowOrder([...rowOrder.reverse()])
        selectcolumnOrder([...columnOrder.reverse()])

    }, [view])

    const [selectedPiece, selectSelectedPiece] = useState<string>('')

    useEffect(() => {
        showMovements(selectedPiece);

    }, [selectedPiece])

    useEffect(() => {
        axios
            .get(backendUrl + 'board')
            .then((res) => {
                setBoard(res.data.board)
                setTurn(res.data.turn)
                setWinner(res.data.winner)
            })
    }, [])

    const initializeBoard = () => {

        axios
            .get(backendUrl + 'initialize')
            .then((res) => {
                setBoard(res.data.board)

                setTurn(res.data.turn)
                setWinner("")
                playBeep()
            })
    }



    const movePieceBackend = (selectedPiece: String, targetSquare: String) => {

        axios({
            method: 'get',
            url: backendUrl + 'play',
            params: { "selectedSquare": selectedPiece, "targetSquare": targetSquare, "promotion": promotionPiece[0] },

        }).then((res) => {
            setBoard(res.data.board)
            setTurn(res.data.turn)
            setWinner(res.data.winner)
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
     * 
     * Replace the targetSquare key entry of board with the selectedSquare key entry.
     * 
     * Remove the selectedSquare key entry afterwards to clean the square.
     * 
     * @param targetSquare The targetted square.
     */
    function movePiece(targetSquare: string) {


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


    const doPromotion = () => {

        setColorPromotion(board[selectedPiece].pieceColor)
        document.getElementById('modal')!.style.visibility = 'visible'

    }

    function selectPromotion(name: string, color: string) {

        selectPromotionPiece(name[0])
        setBoard({ ...board, [selectedPiece]: { 'pieceType': name, 'pieceColor': color, 'availableMovements': [], 'isChecked': false } })
        document.getElementById('modal')!.style.visibility = 'hidden'
        movePiece(targetSquarePromotion)

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
            <img onClick={() => selectView(!view)} src={SwitchButton} className='switchButton' width='30px' />
            <button onClick={initializeBoard}> Reset Board </button>



        </>
    )
}


export default Board
