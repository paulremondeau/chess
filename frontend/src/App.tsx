import './App.scss'

import Board from './components/Board/Board'


function App() {


  return (
    <>
      <div className='body'>
        <div className='header'>
          Scala Chess
        </div>
        <div className='board'>
          <Board />
        </div>
      </div>

    </>
  )
}

export default App
