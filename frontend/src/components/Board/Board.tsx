// Import CSS
import './Board.scss'

// Import components
import Piece from '../Piece/Piece'
import Promotion from '../Promotion/Promotion'
import Timer from '../Timer/Timer'

// Import utils
import { convertSquare } from '../../utils/convertSquare'
import Semaphore from '../../utils/semaphore'

// Import libraries
import { useState, useEffect, useRef } from 'react'

// @ts-ignore
import useSound from 'use-sound';
import moveSfx from '../../assets/move-self.mp3';
import captureSfx from '../../assets/capture.mp3';
import beepSfx from '../../assets/lichess-beep.mp3';
import axios, { AxiosResponse } from 'axios'
const headerConfig = {
    headers: {
        'Cache-Control': 'no-cache, no-store, must-revalidate',
        'Pragma': 'no-cache',
        'Expires': 0
    }
};

const throttler = new Semaphore(1);

// Trivia
import SwitchButton from '../../assets/switch.png'
import backendUrl from '../../../config'

interface Square {
    [key: string]: { 'pieceType': string, 'pieceColor': string, 'availableMovements': string[], 'isChecked': boolean };
}

interface TimePlays {
    [key: string]: number[];
}

function Board() {


    /**
     * The promotion type piece required
     */
    var promotionPiece: string = "_"

    // States
    /// Visualisation
    const [board, setBoard] = useState<Square>({})
    const [sideView, selectSideView] = useState<string>("w")
    const [rowOrder, selectRowOrder] = useState<number[]>([8, 7, 6, 5, 4, 3, 2, 1])
    const [columnOrder, selectcolumnOrder] = useState<number[]>([1, 2, 3, 4, 5, 6, 7, 8])

    var nPieces: number = Object.keys(board).length
    /**
     * Used to play the sounds
     */
    const boardChanged = useRef<boolean>(false)

    /// Move
    const [selectedPiece, selectSelectedPiece] = useState<string>('')

    /// Promotion
    const [colorPromotion, setColorPromotion] = useState<string>("w")
    const [targetSquarePromotion, selectTargetSquarePromotion] = useState<string>("")

    /// Game 
    const [winner, setWinner] = useState<string>("")
    const [turn, setTurn] = useState<string>("")
    const timeLimit: number = 600000
    const [gameClock, setGameClock] = useState<number>(0)




    /// Sounds
    const [playMove] = useSound(moveSfx);
    const [playCapture] = useSound(captureSfx);
    const [playBeep] = useSound(beepSfx);

    /// Timers
    const [timesPlay, setTimesPlay] = useState<TimePlays>({ "w": [], "b": [] })
    /**
     * If a player clocks run to 0, this play loses the game
     * @param color The losing player
     */
    function lostOnTime(color: string) {
        axios({
            method: 'get',
            url: backendUrl + 'lost',
            params: { "loser": color },
            headers: headerConfig.headers

        }).then((res) => {
            updateBoardData(res)
        })
    }

    const whiteTimer = (turn != "" ? <Timer timeLimit={timeLimit}
        turn={turn == "w"}
        lastPlayTimes={timesPlay}
        color={"w"}
        opponent={"b"}
        winner={winner}
        gameClock={gameClock}
        lostOnTime={lostOnTime}
    /> : null)

    const blackTimer = (turn != "" ? <Timer timeLimit={timeLimit}
        turn={turn == "b"}
        lastPlayTimes={timesPlay}
        color={"b"}
        opponent={"w"}
        winner={winner}
        gameClock={gameClock}
        lostOnTime={lostOnTime}
    /> : null)

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
        if (winner != "") {
            playBeep()
        }
    }, [winner])

    /**
     * Change the side view accordingly 
     */
    useEffect(() => {
        if (sideView == "w") {
            selectRowOrder([8, 7, 6, 5, 4, 3, 2, 1])
            selectcolumnOrder([1, 2, 3, 4, 5, 6, 7, 8])

        } else {
            selectRowOrder([1, 2, 3, 4, 5, 6, 7, 8])
            selectcolumnOrder([8, 7, 6, 5, 4, 3, 2, 1])
        }

    }, [sideView])


    /**
     * When a piece is selected, update the possible movements squares
     */
    useEffect(() => {
        showMovements(selectedPiece);
    }, [selectedPiece])

    /**
     * Compare old and new board 
     * @param obj1 Old board
     * @param obj2 New board
     * @returns {boolean} true if the boards are the same
     */
    function compareBoard(obj1: Square, obj2: Square): boolean {

        if ((Object.keys(obj1).length) != (Object.keys(obj2).length)) {
            return false
        } {
            for (const [index, square] of Object.entries(obj1)) {

                if (obj2[index] == undefined) {
                    return false
                }
                if (square.pieceColor != obj2[index].pieceColor ||
                    square.pieceType != obj2[index].pieceType ||
                    square.isChecked != obj2[index].isChecked) {
                    return false
                } else {
                    for (const squareValue of square.availableMovements) {

                        if (!obj2[index].availableMovements.includes(squareValue)) {
                            return false
                        }
                    }
                }

            }
            return true
        }
    }


    /**
     * Update board information with axios backend response.
     * @param res The axios response from the backend.
     */
    function updateBoardData(res: AxiosResponse) {
        new Promise((resolve, _) => {
            setBoard((prevBoard) => {
                if (compareBoard(prevBoard, res.data.board)) {
                    // Board is still the same
                    return { ...prevBoard }

                } else {
                    boardChanged.current = true
                    return { ...res.data.board }
                }
            })
            resolve(0)
        }).then(() => {
            // If board changed, play sound accordingly

            if (boardChanged.current) {

                if (Object.keys(res.data.board).length < nPieces) {
                    nPieces = Object.keys(res.data.board).length

                    playCapture()
                } else {

                    playMove()
                }
                boardChanged.current = false
            }
        })


        setTurn(res.data.turn)
        setWinner(res.data.winner)
        setTimesPlay(res.data.timesPlay)
        setGameClock(res.data.gameClock)

    }

    /**
     * Fetch the board
     * @returns 
     */
    function fetchBoard(): Promise<unknown> {
        return new Promise((resolve) => {
            axios({
                method: 'get',
                url: backendUrl + 'board',
                headers: headerConfig.headers,

            }).then((res) => {
                updateBoardData(res)
            })
            resolve(0)
        })
    }

    const MINUTE_MS = 300;
    /**
     * Fetch backend every 0.3 seconds to see board update.
     */
    useEffect(() => {

        const interval = setInterval(() => {

            throttler.callFunction(fetchBoard, "", "")
        }, MINUTE_MS);

        return () => clearInterval(interval); // This represents the unmount function, in which you need to clear your interval to prevent memory leaks.
    }, [])

    /**
     * Initialize the board from the backend.
     */
    const initializeBoard = () => {
        axios
            .get(backendUrl + 'initialize', headerConfig)
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
    const movePieceBackend = (selectedPiece: String, targetSquare: String): Promise<unknown> => {
        return new Promise((resolve) => {
            axios({
                method: 'get',
                url: backendUrl + 'play',
                params: { "selectedSquare": selectedPiece, "targetSquare": targetSquare, "promotion": promotionPiece },
                headers: headerConfig.headers

            }).then((res) => {

                updateBoardData(res)
            })
            resolve(0)
        })

    }

    /** Event handling function
     * 
     * It handles click and drop event for piece movements.
     *
     * @param e The React mouse event.
     */
    const handleEvent = async (e: React.MouseEvent) => {

        const eventType = e.type
        const target = e.currentTarget
        const componentId = e.currentTarget.id

        if (['click', 'drop'].includes(eventType)) {

            document.getElementById('modal')!.style.visibility = 'hidden'
            if (target.classList.contains('dark') || target.classList.contains('light')) {

                const childDiv: Element = target.children[0]
                const classList: DOMTokenList = childDiv.classList

                if (classList.contains('move')) {
                    if (winner == "") {
                        movePiece(componentId)

                    }
                } else {
                    selectSelectedPiece(componentId)
                }
            } else {
                const target = e.target as Element
                if (target.classList.contains('move')) {
                    if (winner == "") {
                        movePiece(e.currentTarget.id)
                    }
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

        if (board[selectedPiece].pieceColor == turn) {

            if (board[selectedPiece].pieceType == "Pawn" && ((board[selectedPiece].pieceColor == "w" && targetSquare[1] == "8") // Promotion
                || (board[selectedPiece].pieceColor == "b" && targetSquare[1] == "1"))) {
                doPromotion()
                selectTargetSquarePromotion(targetSquare)

            } else {
                // Object.keys(board).indexOf(targetSquare) > -1 ? playCapture() : playMove()

                setBoard((prevState) => {

                    let newState = { ...prevState };
                    newState[targetSquare] = { ...newState[selectedPiece] }
                    delete newState[selectedPiece]
                    return newState
                })

                throttler.callFunction(movePieceBackend, selectedPiece, targetSquare)
                // movePieceBackend(selectedPiece, targetSquare)
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
            <div className='mainBoard'>
                <div className='container'>
                    <div className='timer'>
                        {sideView == "b" ? whiteTimer : blackTimer}
                    </div>
                    <div className='board_table'>
                        {rowOrder.map((rowNumber) => {
                            return (

                                <div className='board_row' key={rowNumber} id={rowNumber.toString()} >
                                    <p className='rowNumber'>{rowNumber}</p>
                                    {
                                        columnOrder.map((columnNumber) => {
                                            let selectedSquare: string = convertSquare(rowNumber, columnNumber);

                                            return (
                                                <div key={selectedSquare} id={convertSquare(rowNumber, columnNumber)}
                                                    className={'board_data ' + ((rowNumber + columnNumber) % 2 == 0 ? 'dark' : 'light')}
                                                    onDrop={(e) => handleEvent(e)}
                                                    onDragOver={event => event.preventDefault()}
                                                    onClick={(e) => handleEvent(e)}

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

                                                </div>)
                                        })
                                    }

                                </div>
                            )
                        }
                        )}
                        <div className='board_row'>
                            {[''].concat(columnOrder.map(p => convertSquare(0, p)).map(p => p[0])).map((columnNumber) => {
                                return (
                                    <div className='board_data columnNumber' key={columnNumber} id={columnNumber.toString()}>
                                        {columnNumber}
                                    </div>)
                            })}

                        </div>
                    </div >
                    <div className='timer'>{sideView == "b" ? blackTimer : whiteTimer}</div>
                    <img onClick={() => selectSideView((prevState) => {
                        if (prevState == "w") {
                            return "b"
                        } else {
                            return "w"
                        }
                    })} src={SwitchButton} className='switchButton' width='30px' />
                    <button onClick={initializeBoard} className='resetButton'> Reset Board </button>
                </div>
            </div>
        </>
    )
}


export default Board
