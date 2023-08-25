import './Header.scss'

import GitHub from '../../assets/github.svg'

function Header() {

    return (
        <>
            <div className="body_header">
                <h1 className="name">Scala Chess</h1>

                <a href="https://paulremondeau.github.io">
                    <img src={GitHub} className="logo" />
                </a>
            </div>
        </>
    )
}
//  target="_blank" rel="noopener noreferrer" />

export default Header