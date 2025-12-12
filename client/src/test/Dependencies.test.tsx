test("Core dependencies are installed", async () => {
  expect(() => import("@vitejs/plugin-react")).not.toThrow();
  expect(() => import("@testing-library/react")).not.toThrow();
  expect(() => import("@testing-library/dom")).not.toThrow();
  expect(() => import("@tailwindcss/vite")).not.toThrow();
  expect(() => import("react")).not.toThrow();
  expect(() => import("tailwindcss")).not.toThrow();
});
