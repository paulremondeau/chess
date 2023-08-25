import Board from '../Board/Board';

import ReactDOM from 'react-dom/client';
import { act } from '@testing-library/react';

let container: HTMLDivElement | null;
import axios from 'axios';

// Mock jest and set the type
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>

beforeEach(() => {
    container = document.createElement('div');
    document.body.appendChild(container);
});

afterEach(() => {
    document.body.removeChild(container!);
    container = null;
});

it('can load', () => {

    // Provide the data object to be returned
    mockedAxios.get.mockResolvedValue({
        data: {
            "winner": "",
            "turn": "w",
            "board": {
                "f7": {
                    "pieceType": "Pawn",
                    "pieceColor": "b",
                    "availableMovements": [
                        "f6",
                        "f5"
                    ],
                    "isChecked": false
                }
            },
        }
    });

    act(() => { ReactDOM.createRoot(container!).render(<Board />); });
    // act(() => { render(<Board />); });


}); 