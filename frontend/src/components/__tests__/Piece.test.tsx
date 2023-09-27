import { render } from '@testing-library/react';
import Piece from '../Piece/Piece';

const renderComponent = () => {
    return render(
        <Piece name="K" color="w" />
    );
};

it('can load', () => {
    const { getAllByRole } = renderComponent();
    expect(getAllByRole("img")).toHaveLength(1)

}); 