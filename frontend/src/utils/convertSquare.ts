function convertSquare(row: number, column: number): string {

    let outputRow: string = row.toString()
    let outputColumn: string = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'][column - 1]

    return outputColumn + outputRow

}

export { convertSquare }

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