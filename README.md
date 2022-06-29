# gingermill
The goal of this project is to find what is longest path of all shortest path between any two wikipedia articles.

So for example between `Gingerbread` and `wind mill` is only one article separation in shortest path.

Idea for this project came from polsih saying "Co ma piernik do wiatraka?" what in direct transaltion means "What has Gingerbread for the windmill" which means "how could that make any sense" 

## How to run
You need to have install docker on your machine then you can run `docker compose up` to setup database. Then you can run `GingermillApplication.kt`

# Gingerbread longest path
So at this point I don't have way to find longest path in whole graph so I decided to fin longest path from Gingerbread.

For polish language wikipedia at 28.06.2022 longest path from Gingerbread is `Gingerbread -> Gare_de_Strasbourg-Roethig`

The path goes through follwoing articles:
```
Piernik
Li%C3%A8ge
Moza
Langres
Gare_de_Langres
Gare_de_Charmes
Gare_de_Bayon
Gare_d%27Einvaux
Gare_de_Blainville_-_Damelevi%C3%A8res
Gare_de_Mont-sur-Meurthe
Gare_de_Lun%C3%A9ville
Gare_de_Saint-Cl%C3%A9ment_-_Laronxe
Gare_de_Chenevi%C3%A8res
Gare_de_M%C3%A9nil-Flin
Gare_d%27Azerailles
Gare_de_Baccarat
Gare_de_Bertrichamps
Gare_de_Thiaville
Gare_de_Raon-l%E2%80%99%C3%89tape
Gare_d%E2%80%99%C3%89tival-Clairefontaine
Gare_de_Saint-Michel-sur-Meurthe
Gare_de_Saint-Di%C3%A9-des-Vosges
Gare_de_Raves_-_Ban-de-Laveline
Gare_de_Lesseux_-_Frapelle
Gare_de_Provench%C3%A8res-sur-Fave
Gare_de_Colroy_-_Lubine
Gare_de_Saales
Gare_de_Bourg-Bruche
Gare_de_Saulxures
Gare_de_Saint-Blaise-la-Roche_-_Poutay
Gare_de_Fouday
Gare_de_Rothau
Gare_de_Schirmeck_-_La_Broque
Gare_de_Russ_-_Hersbach
Gare_de_Wisches
Gare_de_Lutzelhouse
Gare_de_Mullerhof
Gare_d%27Urmatt
Gare_de_Heiligenberg_-_Mollkirch
Gare_de_Gresswiller
Gare_de_Mutzig
Gare_de_Molsheim
Gare_de_Dachstein
Gare_de_Duttlenheim
Gare_de_Duppigheim
Gare_d%27Entzheim-A%C3%A9roport
Gare_de_Holtzheim
Gare_de_Lingolsheim
Gare_de_Strasbourg-Roethig
```
