# gingermill
The goal of this project is to find what is longest path of all shortest path between any two wikipedia articles.

So for example between `Gingerbread` and `wind mill` is only one article separation in shortest path.

Idea for this project came from polsih saying "Co ma piernik do wiatraka?" what in direct transaltion means "What has Gingerbread for the windmill" which means "how could that make any sense" 

## How to run
You need to have install docker on your machine then you can run `docker compose up` to setup database. Then you can run `GingermillApplication.kt`

# Gingerbread the longest path
At the begging I didn't know how to find longest path in whole graph so I decided to fin longest path from Gingerbread.

For polish language wikipedia at 28.06.2022 longest path from Gingerbread is `Piernik -> Gare_de_Strasbourg-Roethig` with `46` articles of separation

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

# Whole wikipedia  the longest path
I figured out I can just check most distant article using bfs for each one instead of checking each combination.

For polish language wikipedia at 11.07.2022 longest path from Gingerbread is `Drzewa_dyskryminacyjne -> Gare_de_Strasbourg-Roethig` with `53` articles of separation

The path goes through follwoing articles:
```
Drzewa_dyskryminacyjne
Drzewo_dyskryminacyjne_(teoria_oblicze%C5%84)
Indeksowanie_term%C3%B3w
Informatyka
Teatr
Tadeusz_Boy-%C5%BBele%C5%84ski
Denis_Diderot
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
