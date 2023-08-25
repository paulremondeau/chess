import './App.scss'

import Board from './components/Board/Board'
import Header from './components/Header/Header'


function App() {


  return (
    <>
      <div className='body'>
        <div className='header'>
          <Header />
        </div>
        <div className='board'>
          <Board />
        </div>
      </div>

    </>
  )
}

export default App
