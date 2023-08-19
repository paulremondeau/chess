import { convertSquare } from "../convertSquare";

describe('Test convert square', function () {
    it('should work', function () {
        expect(convertSquare(1, 1)).toBe("a1")
        expect(convertSquare(8, 8)).toBe("h8")
        expect(convertSquare(1, 8)).toBe("h1")
        expect(convertSquare(8, 1)).toBe("a8")
    })
})
