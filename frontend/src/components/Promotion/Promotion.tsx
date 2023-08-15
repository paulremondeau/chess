import Piece from '../Piece/Piece'
import "./Promotion.scss"

function Promotion({ color, onInteraction }: { color: string, onInteraction: any }) {

    return (
        <>
            <div className='container'>
                <ul>
                    {["Queen", "Rook", "Knight", "Bishop"].map((name) => {
                        return (
                            < li onClick={() => { onInteraction(name, color) }}>
                                <Piece name={name} color={color} />
                            </li>
                        )
                    })}

                </ul>
            </div >
        </>
    )

}

export default Promotion