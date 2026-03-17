import { useEffect, useState } from "react";
import { ScrollView, Text, Image, View, StyleSheet } from "react-native";

/* TO DO LIST:
 * - Print all of a pokemon's type
 * - Convert all colorsByType to hex code and add adjust opacity
 * - Capitalise the first letter of a pokemon's name
 * - Implement tabs to allow users to select the generation
 * - Implement description and more details in the onCLick menu
*/

//Establish pokemon interfaces
interface Pokemon {
  name: string;
  image: string;
  imageBack: string;
  types: PokeType[];
}

interface PokeType {
  type: {
    name: string;
    url: string;
  }
}

//Bind colour to a pokemon's type
const colorsByType = {
  normal: "beige",
  fighting: "orange",
  flying: "aqua",
  poison: "mediumpurple",
  ground: "brown",
  rock: "burlywood",
  bug: "limegreen",
  ghost: "purple",
  steel: "steelblue",
  fire: "red",
  water: "cornflowerblue",
  grass: "forestgreen",
  electric: "goldenrod",
  psychic: "deeppink",
  ice: "mediumturquoise",
  dragon: "royalblue",
  dark: "darkslategrey",
  fairy: "lightpink"
}

export default function Index() {
  const [pokemons, setPokemons] = useState<Pokemon[]>([]);

  useEffect(() => {
    //Fetch Pokemon from PokeAPI
    fetchPokemons();
  }, [])

  async function fetchPokemons() {
    try {
      const response = await fetch("https://pokeapi.co/api/v2/pokemon/?limit=119&offset=905");
      const data = await response.json();

      //Fetch detailed info for each Pokemon in parallel
      const detailedPokemons = await Promise.all(
        data.results.map(async (pokemon: any) => {
          const res = await fetch(pokemon.url);
          const details = await res.json();
          return {
            name: pokemon.name,
            image: details.sprites.front_default, //Front facing sprite
            imageBack: details.sprites.back_default,
            types: details.types,
          };
        })
      );

      setPokemons(detailedPokemons);
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <ScrollView
    contentContainerStyle={{
      gap: 16,
      padding: 16
    }}>
      {pokemons.map((pokemon) => (
        <View key={pokemon.name}
        style={{
          // @ts-ignore (huh this ignores an error on the following line)
          backgroundColor: colorsByType[pokemon.types[0].type.name],
          borderRadius: 20
        }}>
          <Text style={styles.name}>{pokemon.name}</Text>
          <Text style={styles.types}>{pokemon.types[0].type.name}</Text>
          <View
          style={{
            flexDirection: "row"
          }}>
            <Image
              source={{ uri: pokemon.image }}
              style={{ width: 150, height: 150 }}
            />
            <Image
              source={{ uri: pokemon.image }}
              style={{ width: 150, height: 150 }}
            />
          </View>
        </View>
      ))}
    </ScrollView>
  );
}

//Style sheet (refenced as style={styles.<class>})
const styles = StyleSheet.create({
  name: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center'
  },
  types: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'gray',
    textAlign: 'center'
  }
})