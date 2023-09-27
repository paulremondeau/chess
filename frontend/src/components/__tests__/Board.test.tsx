import { render, within } from '@testing-library/react';
import Board from '../Board/Board';
import axios from 'axios';

// Mock jest and set the type
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>

const renderComponent = () => {
    return render(
        <Board />
    );
};


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
            }
        }
    });

    const { container } = renderComponent();
    expect(container.getElementsByClassName("piece")).toHaveLength(1)


}); 