# Recipe Finder App Documentation

## Developers
- Ethan Barker: barkeret@oregonstate.edu
- Jordan P
- Kathleen Ashley: srslyashh@gmail.com
- Getaneh Kudna: kudnag@oregonstate.edu

## Description

Welcome to the Recipe Finder App, Your go-to companion for discovering delicious recipes based on the ingredients you have at hand, written in Kotlin. Explore a world of culinary delights, save your favorite recipes, and seamlessly manage your cooking journey.

## APIs

### Spoonacular API

We rely on the Spoonacular API to provide a rich source of recipe-related data and information. Below are the key features and endpoints of our app's interaction with the Spoonacular API:

- **Recipe Discovery**: We utilize the `recipes/complexSearch` endpoint to offer users a comprehensive search experience. Users can filter and query recipes based on various criteria like cuisine, dietary preferences, and more. The data obtained from this endpoint is used to populate different UI elements, allowing users to find the perfect recipe.

- **Ingredient-Based Search**: The `recipes/findByIngredients` endpoint is the heart of our app's ingredient-based recipe search feature. Users can input the ingredients they have on hand, and this endpoint provides recipes they can cook right away, making meal planning a breeze.

- **Recipe Details**: When users select a specific recipe, we fetch detailed information using the `recipes/{id}/information` endpoint. This data not only populates our recipe cards but also includes image data to make the app visually appealing.

## Features

The Recipe Finder App comes loaded with a range of features to enhance your culinary experience:

1. **Favorite Recipes**: Save your favorite recipes with ease. Our app creates a list of recipes that you like, making it simple to revisit and cook them again.

2. **Share Recipe**: Share your culinary discoveries with friends and family. The app includes an implicit intent feature that allows users to share recipes with other apps or contacts.

3. **Remove Recipes**: Maintain a clutter-free recipe collection by removing recipes you no longer wish to use. Keep your screen organized and filled with only the recipes you love.

4. **Download as PDF (Android Feature)**: Enhance your cooking experience further with the option to download your chosen recipes in PDF format to your phone. This additional Android feature ensures you always have your recipes at your fingertips, even when offline.

With the Recipe Finder App, you can explore new dishes, create a curated list of your favorites, and enjoy hassle-free meal planning. Happy cooking! 🍽️👨‍🍳👩‍🍳

