import renderer from 'react-test-renderer';
import Piece from '../Piece/Piece';

it('can load', () => {
    const component = renderer.create(
        <Piece name="K" color="w"></Piece>,
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();


}); 