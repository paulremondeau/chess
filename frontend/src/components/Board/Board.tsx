import './Board.scss'
import { useState, useEffect } from 'react'

import Piece from '../Piece/Piece'
import { convertSquare } from '../../utils/convertSquare'

// @ts-ignore
import useSound from 'use-sound';

import moveSfx from '../../assets/move-self.mp3';
import captureSfx from '../../assets/capture.mp3';

interface Square {
    [key: string]: { 'name': string, 'color': string, 'movements': string[] };
}

import SwitchButton from '../../assets/switch.png'


function Board() {

    const [playMove] = useSound(moveSfx);
    const [playCapture] = useSound(captureSfx);

    const [board, setBoard] = useState<Square>({
        'a1': { 'name': 'Rook', 'color': 'w', 'movements': [] },
        'b1': { 'name': 'Knight', 'color': 'w', 'movements': [] },
        'c1': { 'name': 'Bishop', 'color': 'w', 'movements': [] },
        'd1': { 'name': 'Queen', 'color': 'w', 'movements': [] },
        'e1': { 'name': 'King', 'color': 'w', 'movements': [] },
        'f1': { 'name': 'Bishop', 'color': 'w', 'movements': [] },
        'g1': { 'name': 'Knight', 'color': 'w', 'movements': [] },
        'h1': { 'name': 'Rook', 'color': 'w', 'movements': [] },

        'a2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'b2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'c2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'd2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'e2': { 'name': 'Pawn', 'color': 'w', 'movements': ['e3', 'e4', 'e7'] },
        'f2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'h2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },
        'g2': { 'name': 'Pawn', 'color': 'w', 'movements': [] },

        'a7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'b7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'c7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'd7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'e7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'f7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'g7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },
        'h7': { 'name': 'Pawn', 'color': 'b', 'movements': [] },

        'a8': { 'name': 'Rook', 'color': 'b', 'movements': [] },
        'b8': { 'name': 'Knight', 'color': 'b', 'movements': [] },
        'c8': { 'name': 'Bishop', 'color': 'b', 'movements': [] },
        'd8': { 'name': 'Queen', 'color': 'b', 'movements': [] },
        'e8': { 'name': 'King', 'color': 'b', 'movements': [] },
        'f8': { 'name': 'Bishop', 'color': 'b', 'movements': [] },
        'g8': { 'name': 'Knight', 'color': 'b', 'movements': [] },
        'h8': { 'name': 'Rook', 'color': 'b', 'movements': [] }
    })
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
        board[selectedSquare].movements.map((id) => {

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

                                                        <Piece name={board[selectedSquare].name} color={board[selectedSquare].color} /></div>

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
        </>
    )
}

export default Board
