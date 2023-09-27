import { render } from '@testing-library/react';

import App from './App';


const renderComponent = () => {
    return render(
        <App />
    );
};

test('Home page renders and contains the word "Scala Chess"', () => {
    const { getAllByText } = renderComponent();
    expect(getAllByText(/Scala Chess/i).length).toBeGreaterThan(0);
});
