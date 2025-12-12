test("Vite environment variables load", () => {
  expect(import.meta.env).toBeDefined();
  expect(typeof import.meta.env.VITE_APP_NAME).toBe("string");
  expect(typeof import.meta.env.BASE_URL).toBe("string");
});
