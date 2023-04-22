# Banked Materials Value
Runelite plugin that calculates the value of your banked materials after processing.
## How it works
This plugin runs off of json item data in the form of raw materials and products. Items classified as products hold information about the skill well as a list of raw materials required to create them. After taking a look at the player's bank, the plugin determines what can be made from the items based on the data held in the json files. If an item isn't defined in the json resources, it won't be available. Finally, the total profit is calculated using the GE price for each possible product and displayed over the materials in the bank as a tooltip.
## Future Improvements
There is no existing model of OSRS items that accomodates what is needed by this plugin to function so that data has to be created manually. This makes adding more products fairly time consuming so that will be an area of continuous improvement. If anyone's reading this; feel free to help. It would also be nice for there to be a window overlay within the bank that shows the most profitable products as well as the most possible profit from items in the player's bank.
