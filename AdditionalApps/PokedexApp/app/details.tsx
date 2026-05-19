import { Stack, useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import { ActivityIndicator, Image, ScrollView, StyleSheet, Text, View } from "react-native";

//Establish pokemon interfaces
interface Pokemon {
  id: number;
  name: string;
  image: string;
  types: PokeType[];
  height: string;
  weight: string;
}

interface PokeType {
  type: {
    name: string;
    url: string;
  }
}

//Bind colour to a pokemon's type
const colorsByType = {
  normal: "#656e6980",
  fighting: "#e17e0680",
  flying: "#15e3e680",
  poison: "#7207c480",
  ground: "#83370580",
  rock: "#ba963d80",
  bug: "#28da1f80",
  ghost: "#a2039d80",
  steel: "#0261b580",
  fire: "#dc070780",
  water: "#0f23d980",
  grass: "#078f4480",
  electric: "#bab40480",
  psychic: "#bb025f80",
  ice: "#0ab2be80",
  dragon: "#1e0ad680",
  dark: "#4a4f4c80",
  fairy: "#f606ca80"
}

export default function Details() {
  const [pokemon, setPokemon] = useState<Pokemon>({ id: 0, name: "loading", image: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/21.png", types: [{ type: { name: "normal", url: "https://pokeapi.co/api/v2/type/1/" } }], height: "0", weight: "0" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  const params = useLocalSearchParams();

  useEffect(() => {
    //Fetch Pokemon from PokeAPI
    fetchPokemon(params.id as string);
  }, [])

  function formatText(text: string) {
    text = String(text).charAt(0).toUpperCase() + String(text).slice(1)
    return text;
  }

  async function fetchPokemon(request: string) {
    setLoading(true);
    try {
      let trueRequest = parseInt(request) - 1;
      let link = "https://pokeapi.co/api/v2/pokemon/?offset=" + trueRequest;
      let response = await fetch(link);
      let data = await response.json();

      //Fetch detailed info for each Pokemon in parallel
      const detailedPokemons = await Promise.all(
        data.results.map(async (pokemon: any) => {
          const res = await fetch(pokemon.url);
          const details = await res.json();
          return {
            id: details.id,
            name: formatText(pokemon.name),
            image: details.sprites.front_default, //Front facing sprite
            types: details.types,
            height: details.height,
            weight: details.weight,
          };
        })
      );
      
      setPokemon(detailedPokemons[0]);
      setError(false);
    } catch (error) {
      console.log(error);
      setError(true);
    }
    setLoading(false);
  }

  return (
    // @ts-ignore
    <View style={{ backgroundColor: colorsByType[pokemon.types[0].type.name], height: "100%" }}>
      <Stack.Screen options={{ title: params.name as string }} />

      {loading && !error && <ActivityIndicator size="large" style={styles.loading} />}

      {error && !loading &&
        <Text>
          Error: Failed to load Pokémon.
        </Text>
      }

      {!loading && !error &&
        <ScrollView>
          <Image
            source={{ uri: pokemon.image }}
            style={{ width: 300, height: 300, alignSelf: 'center' }}
          />
          <View style={styles.infoCard}>
            <Text style={styles.name}>
              {pokemon.name}
            </Text>
            <View style={styles.cardRow}>
              {//@ts-ignore
              <Text style={{ fontSize: 17, fontWeight: 600, color: colorsByType[pokemon.types[0].type.name] }}>
                {formatText(pokemon.types[0].type.name)}
              </Text>
              }
              {pokemon.types[1] &&
                //@ts-ignore
                <Text style={{ fontSize: 17, fontWeight: "600", color: colorsByType[pokemon.types[1].type.name] }}>
                  {formatText(pokemon.types[1].type.name)}
                </Text>
              }
            </View>
            <Text style={styles.size}>
              Height: {pokemon.height}cm
            </Text>
            <Text style={styles.size}>
              Weight: {pokemon.weight}00g
            </Text>
          </View>
        </ScrollView>
      }
    </View>
  );
}

//Style sheet (refenced as style={styles.<class>})
const styles = StyleSheet.create({
  infoCard: {
    backgroundColor: "#e4e4e4",
    marginHorizontal: 20,
    marginBottom: 20,
    paddingHorizontal: 20,
    paddingTop: 10,
    paddingBottom: 20,
    borderRadius: 20,
    shadowColor: "#000000",
    shadowOpacity: 0.05,
    shadowOffset: {
      height: 0,
      width: 1,
    },
    shadowRadius: 4,
  },
  cardRow: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 5,
  },
  loading: {
    backgroundColor: "#fff",
    height: "100%",
  },
  name: {
    fontSize: 20,
    fontWeight: 700,
    alignSelf: "center"
  },
  size: {
    fontSize: 14,
    fontWeight: 500
  }
})