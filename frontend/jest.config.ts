import type { Config } from 'jest';

const config: Config = {
    // The root of your source code, typically /src
    // `<rootDir>` is a token Jest substitutes
    roots: ["<rootDir>/src"],

    // Jest transformations -- this adds support for TypeScript
    // using ts-jest
    transform: {
        "^.+\\.tsx?$": "ts-jest",
        "\\.svg": "<rootDir>/src/__mocks__/fileMock.ts",
        "\\.s?css": "<rootDir>/src/__mocks__/fileMock.ts",
        "\\.mp3": "<rootDir>/src/__mocks__/fileMock.ts",
        "\\.png": "<rootDir>/src/__mocks__/fileMock.ts"
    },

    // Test spec file resolution pattern
    // Matches parent folder `__tests__` and filename
    // should contain `test` or `spec`.
    testRegex: "(/__tests__/.*|(\\.|/)(test|spec))\\.tsx?$",

    testEnvironment: 'jsdom',


    // Module file extensions for importing
    moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json", "node"]
};

export default config;