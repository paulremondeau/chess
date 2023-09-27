import { fireEvent, render } from '@testing-library/react';
import Promotion from '../Promotion/Promotion';

var clickCount = -1

const renderComponent = () => {
    return render(
        <Promotion color="w"
            onInteraction={() => { clickCount += 1 }}
        />
    );
};


it('Displays four clickable images', async () => {

    const { findAllByRole } = renderComponent();
    const items = await findAllByRole("listitem")
    expect(items).toHaveLength(4)
    for (const [index, item] of items.entries()) {
        await fireEvent.click(item)
        expect(clickCount).toBe(index)
    }

}); 