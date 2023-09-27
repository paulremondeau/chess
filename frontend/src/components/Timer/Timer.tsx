// Import CSS
import './Timer.scss'

import { useRef, useEffect } from 'react'

import { Duration } from "luxon";

interface TimePlays {
    [key: string]: number[];
}

function Timer({ timeLimit,
    turn,
    lastPlayTimes,
    color,
    opponent,
    winner,
    gameClock,
    lostOnTime, // for losing on time
}:
    {
        timeLimit: number,
        turn: boolean,
        lastPlayTimes: TimePlays,
        color: string,
        opponent: string,
        winner: string,
        gameClock: number,
        lostOnTime: any,

    }) {

    const timer = useRef<number>(timeLimit)
    if (timer.current == 0) {
        lostOnTime(color)
    }

    function timePlayTurn(plays: number[], playsOpponent: number[]): number {

        const tempArray: number[] = plays.concat(gameClock)

        if (plays.length < playsOpponent.length) { // Black turn to play
            null
        } else {
            tempArray.shift()
        }

        return tempArray.map((v, i) => v - playsOpponent[i]).reduce((sum, num) => sum + num)

    }


    function timePlayNotTurn(plays: number[], playsOpponent: number[]): number {

        const tempArray: number[] = plays

        if (plays.length > playsOpponent.length) { // Black turn to play
            tempArray.shift()
        } else {
            null
        }

        return tempArray.map((v, i) => v - playsOpponent[i]).reduce((sum, num) => sum + num)

    }


    useEffect(() => {
        if (winner == "") {
            if (turn) {
                timer.current = Math.max(0, (lastPlayTimes[color].length > 0 ? timeLimit - timePlayTurn(lastPlayTimes[color], lastPlayTimes[opponent]) : timeLimit))
            } else {
                timer.current = Math.max((lastPlayTimes[color].length > 1 ? timeLimit - timePlayNotTurn(lastPlayTimes[color], lastPlayTimes[opponent]) : timeLimit))
            }
        }

    }, [gameClock])


    return (
        <>
            <div className={'box ' + (turn ? 'playing' : 'waiting')}>
                <p className={'time'}>
                    {isNaN(timer.current) ? 10000 : Duration.fromMillis(timer.current).toFormat("mm:ss")}
                </p>

            </div>
        </>
    )
}

export default Timer 