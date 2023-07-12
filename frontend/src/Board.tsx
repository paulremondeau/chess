import './Board.scss'
import { useState } from 'react'


function Board() {

    const [board, setBoard] = useState()

    return (
        <>
            <table className='board'>
                {[8, 7, 6, 5, 4, 3, 2, 1].map((rowNumber) => {
                    return (
                        <tr className='row'>
                            <p className='rowNumber'>{rowNumber}</p>
                            {[1, 2, 3, 4, 5, 6, 7, 8].map((squareNumber) => {
                                return (
                                    <td className={(rowNumber + squareNumber) % 2 == 0 ? 'dark' : 'light'

                                    }>
                                        <img src='src/assets/chess_pieces/Chess_bdt45.svg'></img>
                                    </td>)
                            })}

                        </tr>
                    )
                }
                )}
                <tr className='row'>
                    {['', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'].map((columnNumber) => {
                        return (
                            <td className='columnNumber'>
                                {columnNumber}
                            </td>)
                    })}

                </tr>
            </table >

        </>
    )
}

export default Board
