import {render, screen} from "@testing-library/react";
import "@testing-library/jest-dom"
import App from "./App"

test("renders app without crashing", () => {
    render(<App />);
    expect(screen.getByText(/Hello World/i)).toBeInTheDocument()
    expect(screen.getByText(/btn/i)).toBeDefined();
})

