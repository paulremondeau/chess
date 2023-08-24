// Import CSS
import './Timer.scss'

import { useRef, useEffect } from 'react'

import { Duration, DateTime } from "luxon";

interface TimePlays {
    [key: string]: number[];
}

function Timer({ timeLimit,
    turn,
    lastPlayTimes,
    color,
    opponent,
    onInteraction,
    foo }:
    {
        timeLimit: number,
        turn: boolean,
        lastPlayTimes: TimePlays,
        color: string,
        opponent: string,
        onInteraction: any,
        foo: React.MutableRefObject<number>
    }) {

    const timer = useRef<number>(timeLimit)

    function timePlayTurn(plays: number[], playsOpponent: number[]): number {

        const tempArray: number[] = plays.concat(DateTime.now().toUTC().toMillis())

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
        if (turn) {
            timer.current = (lastPlayTimes[color].length > 0 ? timer.current - timePlayTurn(lastPlayTimes[color], lastPlayTimes[opponent]) : timeLimit)
        } else {
            timer.current = (lastPlayTimes[opponent].length > 0 ? timer.current - timePlayNotTurn(lastPlayTimes[color], lastPlayTimes[opponent]) : timeLimit)
        }
    }, [foo.current])


    useEffect(() => {

        const interval = setInterval(() => {
            if (turn && lastPlayTimes[color].length > 0) {
                timer.current = Math.max(0, timer.current - 1000)
            }
        }, 1000);
        return () => clearInterval(interval);
    }, [turn]);


    const logMe = () => {
        // console.log(DateTime.now().toUTC().toMillis())
        console.log(lastPlayTimes)
        console.log(lastPlayTimes[color].length > 0)
        console.log(turn)
        console.log(timePlayTurn(lastPlayTimes[color], lastPlayTimes[opponent]))

    }

    return (
        <>
            <button onClick={logMe}>LogMe</button>
            <div className={'box ' + (turn ? 'playing' : 'waiting')}>
                <div className={'time'}>
                    {[...Duration.fromMillis(timer.current).toFormat("mm:ss")].map(c => {
                        return (
                            <p>{c}</p>
                        )
                    })}
                </div>

            </div>
        </>
    )
}

export default Timer 