import { Link } from "expo-router";
import { useEffect, useState } from "react";
import { ActivityIndicator, Image, ScrollView, StyleSheet, Text, TouchableOpacity, View } from "react-native";

//Establish pokemon interfaces
interface Pokemon {
  id: number;
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

const REGIONS = [
  { id: "1", label: "Kanto", "value": "?limit=151" },
  { id: "2", label: "Johto", "value": "?limit=100&offset=151" },
  { id: "3", label: "Hoenn", "value": "?limit=135&offset=251" },
  { id: "4", label: "Sinnoh", "value": "?limit=107&offset=386" },
  { id: "5", label: "Unova", "value": "?limit=156&offset=493" },
  { id: "6", label: "Kalos", "value": "?limit=72&offset=649" },
  { id: "7", label: "Alola", "value": "?limit=88&offset=721" },
  { id: "8", label: "Galar", "value": "?limit=89&offset=809" },
  { id: "9", label: "Paldea", "value": "?limit=120&offset=905" },
  { id: "10", label: "Hisui", "value": "?limit=7&offset=898" },
  { id: "11", label: "Misc", "value": "?offset=1025" }
];

export default function Index() {
  const [pokemons, setPokemons] = useState<Pokemon[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  const [selectedRegion, setSelectedRegion] = useState<string>(REGIONS[8].label);

  function randomRegion(): number {
    return Math.floor(Math.random() * 10);
  }

  useEffect(() => {
    //Fetch Pokemon from PokeAPI
    let region = randomRegion();
    fetchPokemon(REGIONS[region].value);
    setSelectedRegion(REGIONS[region].label);
  }, [])

  function formatText(text: string) {
    let textArray = text.split("-");
    for (let i = 0; i < textArray.length; i++) {
      textArray[i] = String(textArray[i]).charAt(0).toUpperCase() + String(textArray[i]).slice(1);
    }
    return textArray.join(" ");
  }

  async function fetchPokemon(request: string) {
    setLoading(true);
    try {
      let link = "https://pokeapi.co/api/v2/pokemon/" + request;
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
            imageBack: details.sprites.back_default,
            types: details.types,
          };
        })
      );

      setPokemons(detailedPokemons);
      setError(false);
    } catch (error) {
      setError(true);
      console.log(error);
    }
    setLoading(false);
  }

  return (
    <View>
      <TouchableOpacity
        style={styles.dropdown}
        onPress={() => setShowDropdown(!showDropdown)}
      >
        <Text style={styles.dropdownItemTextSelected}>
          {selectedRegion}
        </Text>
        <Text style={styles.dropdownIcon}>
          {showDropdown ? '▲' : '▼'}
        </Text>
      </TouchableOpacity>
      {showDropdown &&
        <View>
          {REGIONS.map((region) => (
            <TouchableOpacity
              key={region.id}
              style={[
                styles.dropdownItem,
                selectedRegion === region.id && styles.dropdownItemSelected
              ]}
              onPress={() => {
                setSelectedRegion(region.label);
                setShowDropdown(false);
                fetchPokemon(region.value);
              }}>
              <Text>{region.label}</Text>
            </TouchableOpacity>
          ))}
        </View>
      }

      {loading && !error && <ActivityIndicator size="large" />}

      {error && !loading &&
        <Text>
          Error: Failed to load Pokémon.
        </Text>
      }

      {!loading && !error &&
        <ScrollView
          contentContainerStyle={{
            gap: 16,
          }}>
          <View style={{ paddingBottom: 110, marginHorizontal: 12 }}>
            {pokemons.map((pokemon) => (
              <Link key={pokemon.id}
                href={{ pathname: "/details", params: { id: pokemon.id, name: pokemon.name } }}
                style={{
                  // @ts-ignore (huh this ignores an error on the following line)
                  backgroundColor: colorsByType[pokemon.types[0].type.name],
                  borderRadius: 20,
                  marginVertical: 8,
                }}>
                <View style={{ padding: 20 }}>
                  <View
                    style={styles.cardRow}>
                    <Text style={styles.name}>#{pokemon.id} • {pokemon.name}</Text>
                  </View>
                  <View
                    style={styles.cardRow}>
                    <Text style={styles.types}>{formatText(pokemon.types[0].type.name)}</Text>
                    {pokemon.types[1] &&
                      <Text style={styles.types}> / {formatText(pokemon.types[1].type.name)}</Text>
                    }
                  </View>
                  <View
                    style={{
                      flexDirection: "row",
                      justifyContent: "center"
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
              </Link>
            ))}
          </View>
        </ScrollView>
      }
    </View>
  );
}

//Style sheet (refenced as style={styles.<class>})
const styles = StyleSheet.create({
  cardRow: {
    flexDirection: 'row',
    justifyContent: 'center'
  },
  name: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center'
  },
  types: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center'
  },
  loading: {
    backgroundColor: "#fff",
    paddingVertical: 50,
    height: "100%"
  },
  dropdown: {
    marginHorizontal: 12,
    marginTop: 12,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#E7EBF2",
    borderRadius: 8,
    padding: 14,
    marginBottom: 12,
  },
  dropdownTextSelected: {
    fontSize: 15,
    fontWeight: '500',
  },
  dropdownIcon: {
    fontSize: 12,
  },
  dropdownMenu: {
    backgroundColor: "#fff",
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#E7EBF2",
    marginTop: -10,
    marginHorizontal: 12, 
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 5,
    maxHeight: 340,
  },
  dropdownItem: {
    padding: 14,
    borderBottomWidth: 1,
    borderBottomColor: "#E7EBF2",
  },
  dropdownItemSelected: {
    backgroundColor: "#EEF2FF",
  },
  dropdownItemText: {
    fontSize: 15,
  },
  dropdownItemTextSelected: {
    fontWeight: '600',
  },
})