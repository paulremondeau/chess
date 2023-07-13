import './Piece.scss'
import { useRef, useState, useEffect } from 'react'


function Piece({ name, color, movements }: { name: string, color: string, movements?: [number, number][] }) {


    const [availableMovements, setAvailableMovements] = useState([])

    useEffect(() => {
        setAvailableMovements(availableMovements);
    }, [movements])



    return (
        <>

            <img src={'src/assets/chess_pieces/' + name + '_' + color + '.svg'}></img>
        </>
    )

}

export default Piece