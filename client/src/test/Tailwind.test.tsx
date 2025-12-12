import {render} from "@testing-library/react";

function TailwindButton() {
    return <button className="bg-blue-500 text-white p-2 rounded">Click</button>
}

test("TailwindCSS classes compile", () => {
    const {container} = render(<TailwindButton />);
    const button = container.querySelector("button");

    expect(button).toBeTruthy();
    expect(button?.className).toContain("bg-blue-500");
    expect(button?.className).toContain("p-2")
})
