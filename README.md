# TDLKingdoms
Unfinished project.

# Kingdom Creation
Command: /kingdom create (name)
Command: /kingdom sethome
This opens a GUI with the GUI name set to the name the user has put in. In this GUI players can choose their kingdom template (This I will explain later), when they choose their kingdom theme they will get another GUI which asks the question are you sure you want to build your kingdom here? If they click green concrete it will confirm, if they click red concrete it will cancel. The cancel button cancels everything so it doesn’t save anything the user did since doing /kingdom create, only when they click confirm it will save the kingdom and it will start building the template they chose 1 block in front of them. Kingdoms cannot be built in WorldGuard regions. Now the creator will get a message which says do /kingdoms sethome to set a home for all kingdom members (This can only be done within the borders of their kingdom)

Permission: kingdom.create
Permission: kingdom.sethome

# Kingdom Home
Command: /kingdom home
This teleports the player to the location that their kingdom creator has set the kingdom home to.

Permission: kingdom.home

# Kingdom Themes
So before I explain what kingdom themes are I would like to tell you about kingdom templates.
Kingdom templates are buildings that I will provide to the programmer. So my idea is when a player starts a kingdom they will start off with like the level 1 kingdom, just for example let’s say kingdom level 1 is a wooden hut, kingdom level 2 would be a house with a garden, kingdom level 3 would be a mansion, kingdom level 4 would be a kingdom with walls. So what I would want is when they create the kingdom and click confirm it will build the wooden hut in 1 block in front of the player (Because when kingdoms are created they obviously start off at level 1). When they upgrade the hut, the hut will change into the house with a garden and so on. 

So now for kingdom themes, let’s say we have Heaven, Medieval and Hell. Let’s say the wooden hut I mentioned earlier would be the Medieval theme, so if the player chooses Heaven it would be a different kind of hut with different blocks and same goes for the Hell theme (Of course I will provide all buildings for each theme and level). 
I’m currently not entirely sure yet what themes I want so we will discuss when we established that it’s possible for the programmer to make this plugin.

# Kingdom Editing
Command: /Kingdom edit
This command can naturally only be executed if you own a kingdom. 
This opens a GUI with the GUI name set to the user’s kingdom name. In this editing GUI the user will have a few settings:
⁃	Editing Kingdom name (Name Tag)
	*This will close the GUI and ask the user to type in a new name for their Kingdom.
⁃	Public or Private (Public (Green Concrete) = Anyone can join the kingdom / Private (Red Concrete)= People can only join with invite)
⁃	Members (Steve head)
	*This will open a new GUI with each member’s head in a slot, if player clicks on a head it will open a new  GUI and it will give them 3 options. 
Promote(XP Bottle) (If selected player is already the highest rank (that would be Co-Ruler) Don’t show promote *Ranking will be explained in Kingdom Member Ranking header), 
Demote (Red Concrete) (If selected player is already the lowest rank don’t show the demote button) 
Remove(Barrier) the member.
⁃	Upgrade Kingdom (XP Bottle) When player clicks on this it will open a new GUI which asks the player to confirm, again with Green Concrete to confirm and Red Concrete to cancel. When player clicks confirm the configured amount of upgrade cost will be deducted from player’s balance and depending on what level they are their current Template will be built into the next level’s Template
⁃	Kingdom Bank Balance (Golden Ingot) (This only shows the balance does nothing else)

Permission: kingdom.edit

#Kingdom Collective Bank
Command: /kingdom donate (amount)
Members can donate money to the kingdom to upgrade the kingdom so they will get better mines, bigger houses and more shops. The money in the Kingdom Collective Bank can only be used to upgrade the kingdom and only the Kingdom Creator and Co-Ruler can do that.
All members can donate to the Kingdom Collective Bank by doing /kingdom donate (amount)

Permission: kingdom.donate

# Kingdom Upgrading
Command: /Kingdom upgrade
This opens the same GUI as if the player would have clicked the Upgrade Kingdom from the Kingdom Editing GUI, so they will be shown the confirm or cancel options.

Kingdom creators and Co-Rulers can’t upgrade kingdoms with their own money, only the money from the Kingdom Collective Bank will be used. If there aren’t sufficient funds on there it will display the message: Your Kingdom Collective Bank has insufficient funds, do /kingdom donate (amount) to deposit money.

Permission: kingdom.edit


# Kingdom Joining
Command: /Kingdom join (name)
If kingdom is public, player will join kingdom. If kingdom is private it will tell the player that they need an invite to join the kingdom.

Permission: kingdom.join

# Kingdom land
Kingdoms have a preset amount of owned land, how big the land is depends on what level the kingdom is.
Within these borders only members of the kingdom can start a fight with non-members, which enables pvp for them both while non-members can’t start a fight.
I will come back to the land values later because it depends on the template sizes.

# Kingdom Members

All kingdoms have, as mentioned earlier, houses in them. These houses can only be bought by members of the kingdom and there is a maximum of 1 house per person.


# Kingdom Member Ranking
The kingdom creator can promote members to several ranks:
⁃	Co-Ruler (This role will have access to /kingdom edit but can only use the remove player feature (Of course they can’t remove the kingdom creator), Kingdom upgrade and set the kingdom to public or private. Also they will be able to build in the Main Building.)
⁃	General (This role can remove members from the kingdom that have a lower rank than they do)
⁃	Member (Default Role)

# Kingdom Shops

All kingdoms have, as mentioned earlier shops in them. These shops can only be bought by members of the kingdom and there is a maximum of 1 shop per person.

All shops have a standard amount of chests which can be used for players to put items in to sell. 
To make a shop they will need to put the item they want to sell in it, then when they close the chest the item the player put in the chest should be displayed (floating) on top of the chest. They will get prompted to define a price and amount for the item by typing in chat and then a sign will be created on the chest which gives the name of the item that’s being sold, the amount and  the price.

# Kingdom Mines
Kingdom mines can only be used by kingdom members
Kingdom mines are the same concept as prison mines so the mines will automatically reset after a configurable amount of time:

Kingdom level 1 mine resources (Configurable spawn ratio):
⁃	Stone
⁃	Coal Ore
⁃	Iron Ore

Kingdom Level 2 mine resources (Configurable spawn ratio):
⁃	Stone
⁃	Coal Ore
⁃	Iron Ore
⁃	Gold Ore

Kingdom Level 3 mine resources (Configurable spawn ratio):
⁃	Stone
⁃	Iron Ore
⁃	Gold Ore
⁃	Lapis Lazuli Ore
⁃	Diamond Ore

Kingdom Level 4 mine resources (Configurable spawn ratio):
⁃	Stone
⁃	Iron Ore
⁃	Gold Ore
⁃	Lapis Lazuli Ore
⁃	Diamond Ore

Kingdom Level 5 mine resources (Configurable spawn ratio):
⁃	Stone
⁃	Iron Ore
⁃	Gold Ore
⁃	Lapis Lazuli Ore
⁃	Diamond Ore
⁃	Emerald Ore

# Building within the kingdom:
The creator of the kingdom will be able to build ONLY within the walls of the main building so he can’t modify the exterior at all, same goes for members of the kingdom which either bought a house or a shop. They can ONLY modify the inside and not the exterior. When the kingdom owner upgrades the kingdom everything will of course get deleted because the template of the next level overrides the template of the previous level. This includes everything that players have placed inside their building territory.

So what I need it to do is give all the blocks the players have placed inside their building territory (So for the player who bought a house and placed a wooden plank in there, he would get his wooden plank back into his inventory even if he is offline. And same goes for for example the kingdom owner who built a diamond throne in his main building, he would get all that back into his inventory even if he is offline.
Also the chests will get removed of course including the items people put in them. So what I had in mind is when people put chests down, every chest they put down will be stored somewhere and linked to the player. Everytime the player removes or places something in the chest it should update the contents so nothing gets removed or duplicated accidentally. So when everything gets deleted they could do /Kingdom chests and they would get a GUI with all the chests they have placed down before the chests got removed (Because of the kingdom upgrade) and if they click on one it will put that chest into their inventory and if they place it it will contain all the items. If you have any better suggestion on how to handle this please discuss it with me :).
Players will get a notification when they’re online which will say that the kingdom has been upgraded and all blocks they have built will be restored into their inventory and all chests will be stored and they can withdraw the chests with /kingdom chests, offline players will get the same notification as soon as they go online for the first time since the kingdom upgrade.

# Kingdom Chat
Command: /Kingdom chat 
This command naturally only works if you’re part of a kingdom or own one.
This command switches your chat to only the people who are in the same kingdom as you, or if they type it again it switches back to normal chat.

Permission: kingdom.chat

# Kingdom Leveling
Kingdom Level 1
⁃	Level 1 Template
⁃	Max 5 members
⁃	Max 3 shops
⁃	Level 1 Mine

Kingdom Level 2 (Configurable amount of upgrade cost)
⁃	Level 2 Template
⁃	Max 10 Members
⁃	Max 6 Shops
⁃	Level 2 Mine

Kingdom Level 3 (Configurable amount of upgrade cost)
⁃	Level 3 Template
⁃	Max 20 Members
⁃	Max 12 Shops
⁃	Level 3 Mine

Kingdom Level 4 (Configurable amount of upgrade cost)
⁃	Level 4 Template
⁃	Max 30 Members
⁃	Max 22 Shops
⁃	Level 4 Mine

Kingdom Level 5 (Configurable amount of upgrade cost)
⁃	Level 5 Template
⁃	Max 40 Members
⁃	Max 32  Shops
⁃	Level 5 Mine

# Prevent kingdom collision
So each time a kingdom gets to a higher level the size increases aswell as their owned land, there could be a problem where someone made a kingdom close to another kingdom and as they grow they collide with each other. We can’t have that so that’s why we won’t let the player create a kingdom if it’s not far enough of another kingdom’s “Full size”. So let’s say there is a kingdom 50 blocks away from me, that kingdom is level 2. My kingdom would be level 1 when I start and that should not be a problem because it probably won’t be 50 blocks. but when we both grow it will become a problem. So the distance between each kingdom must be enough to let them be able to grow to full size without colliding, if this isn’t the case it will say: Can’t create a kingdom here, too close to another kingdom. Same principal goes for making a kingdom too close to a world guard region.
