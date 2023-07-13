function convertSquare(row: number, column: number): string {

    let outputRow: string = row.toString()
    let outputColumn: string = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'][column - 1]

    return outputColumn + outputRow

}

export { convertSquare }