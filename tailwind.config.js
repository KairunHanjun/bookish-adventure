/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/main/resources/static/**/*.{html,js}"],
    variants: {
     fontSize: ['responsive', 'hover', 'focus'],
    },
    theme: {
      extend: {
        fontFamily: {
          'Playfair': ['Playfair', 'sans-serif'],
          'Inter': ['Inter', 'sans-serif'],
          'ClickerScript': ['ClickerScript', 'sans-serif']
        }
      },
    },
    plugins: [],
  }