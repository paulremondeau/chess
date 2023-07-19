import './Board.scss'
import { useState, useEffect } from 'react'

import Piece from '../Piece/Piece'
import { convertSquare } from '../../utils/convertSquare'

// @ts-ignore
import useSound from 'use-sound';

import moveSfx from '../../assets/move-self.mp3';
import captureSfx from '../../assets/capture.mp3';

interface Square {
    [key: string]: { 'pieceType': string, 'pieceColor': string, 'availableMovements': string[] };
}

import SwitchButton from '../../assets/switch.png'

import axios from 'axios'

const backendUrl: String = 'http://127.0.0.1:8080/'


function Board() {

    const [playMove] = useSound(moveSfx);
    const [playCapture] = useSound(captureSfx);

    // const [board, setBoard] = useState<Square>({
    //     'a1': { 'pieceType': 'Rook', 'pieceColor': 'w', 'availableMovements': [] },
    //     'b1': { 'pieceType': 'Knight', 'pieceColor': 'w', 'availableMovements': [] },
    //     'c1': { 'pieceType': 'Bishop', 'pieceColor': 'w', 'availableMovements': [] },
    //     'd1': { 'pieceType': 'Queen', 'pieceColor': 'w', 'availableMovements': [] },
    //     'e1': { 'pieceType': 'King', 'pieceColor': 'w', 'availableMovements': [] },
    //     'f1': { 'pieceType': 'Bishop', 'pieceColor': 'w', 'availableMovements': [] },
    //     'g1': { 'pieceType': 'Knight', 'pieceColor': 'w', 'availableMovements': [] },
    //     'h1': { 'pieceType': 'Rook', 'pieceColor': 'w', 'availableMovements': [] },

    //     'a2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'b2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'c2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'd2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'e2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': ['e3', 'e4', 'e7'] },
    //     'f2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'h2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },
    //     'g2': { 'pieceType': 'Pawn', 'pieceColor': 'w', 'availableMovements': [] },

    //     'a7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'b7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'c7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'd7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'e7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'f7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'g7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },
    //     'h7': { 'pieceType': 'Pawn', 'pieceColor': 'b', 'availableMovements': [] },

    //     'a8': { 'pieceType': 'Rook', 'pieceColor': 'b', 'availableMovements': [] },
    //     'b8': { 'pieceType': 'Knight', 'pieceColor': 'b', 'availableMovements': [] },
    //     'c8': { 'pieceType': 'Bishop', 'pieceColor': 'b', 'availableMovements': [] },
    //     'd8': { 'pieceType': 'Queen', 'pieceColor': 'b', 'availableMovements': [] },
    //     'e8': { 'pieceType': 'King', 'pieceColor': 'b', 'availableMovements': [] },
    //     'f8': { 'pieceType': 'Bishop', 'pieceColor': 'b', 'availableMovements': [] },
    //     'g8': { 'pieceType': 'Knight', 'pieceColor': 'b', 'availableMovements': [] },
    //     'h8': { 'pieceType': 'Rook', 'pieceColor': 'b', 'availableMovements': [] }
    // })
    const [board, setBoard] = useState<Square>({})
    const [view, selectView] = useState<boolean>(false)

    const [rowOrder, selectRowOrder] = useState<number[]>([8, 7, 6, 5, 4, 3, 2, 1])
    const [columnOrder, selectcolumnOrder] = useState<number[]>([1, 2, 3, 4, 5, 6, 7, 8])

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
            })
    })

    const initializeBoard = () => {

        axios
            .get(backendUrl + 'initialize')
            .then((res) => {
                setBoard(res.data.board)
            })
    }

    const movePieceBackend = (selectedPiece: String, targetSquare: String) => {

        axios({
            method: 'get',
            url: backendUrl + 'play',
            params: { "selectedSquare": selectedPiece, "targetSquare": targetSquare },

        }).then((res) => {
            setBoard(res.data.board)
        })


    }

    /** Click event handler.
     *
     * @param e 
     */
    const handleClick = (e: React.MouseEvent) => {

        console.log(e.type)

        const eventType = e.type
        const target = e.currentTarget
        const componentId = e.currentTarget.id



        if (eventType == 'click') {


            const className: string = target.className

            if (className == 'dark' || className == 'light') {

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

        Object.keys(board).indexOf(targetSquare) > -1 ? playCapture() : playMove()


        board[targetSquare] = board[selectedPiece]
        delete board[selectedPiece]

        setBoard({
            ...board,

        });

        movePieceBackend(selectedPiece, targetSquare)

        selectSelectedPiece('')

    }


    return (
        <>

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
