import './Piece.scss'
import { useState, useEffect } from 'react'

import RookB from '../../assets/chess_pieces/Rook_b.svg'
import RookW from '../../assets/chess_pieces/Rook_w.svg'

import QueenB from '../../assets/chess_pieces/Queen_b.svg'
import QueenW from '../../assets/chess_pieces/Queen_w.svg'


import KnightB from '../../assets/chess_pieces/Knight_b.svg'
import KnightW from '../../assets/chess_pieces/Knight_w.svg'


import BishopB from '../../assets/chess_pieces/Bishop_b.svg'
import BishopW from '../../assets/chess_pieces/Bishop_w.svg'


import KingB from '../../assets/chess_pieces/King_b.svg'
import KingW from '../../assets/chess_pieces/King_w.svg'


import PawnB from '../../assets/chess_pieces/Pawn_b.svg'
import PawnW from '../../assets/chess_pieces/Pawn_w.svg'

const convertDict: {
    [key: string]: string;
} = {

    'Rook_b': RookB,
    'Rook_w': RookW,

    'Queen_b': QueenB,
    'Queen_w': QueenW,

    'Knight_b': KnightB,
    'Knight_w': KnightW,

    'Bishop_b': BishopB,
    'Bishop_w': BishopW,

    'King_b': KingB,
    'King_w': KingW,

    'Pawn_b': PawnB,
    'Pawn_w': PawnW,



}


function Piece({ name, color }: { name: string, color: string, movements?: [number, number][] }) {

    return (
        <>
            <img src={convertDict[name + '_' + color]}></img>
        </>
    )

}

export default Piece