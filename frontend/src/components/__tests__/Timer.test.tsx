import { render } from '@testing-library/react';
import Timer from '../Timer/Timer';

const renderComponent = () => {
    return render(
        <Timer timeLimit={600000}
            turn={true}
            lastPlayTimes={{}}
            color="w"
            opponent="b"
            winner="w"
            gameClock={100000}
            lostOnTime="" />
    );
};


it('Displays the time correctly if no moves were played', () => {

    const { getByText } = renderComponent();
    expect(getByText(/10:00/i)).toBeTruthy

}); 