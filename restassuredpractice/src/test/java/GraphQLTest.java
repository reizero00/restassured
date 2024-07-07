import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class GraphQLTest {

    @Test
    public void graphQLQueryTest() {
        String response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .body("{\"query\":\"query(\\n" + //
                                        "  $characterId: Int!,\\n" + //
                                        "  $locationId: Int!\\n" + //
                                        ")\\n" + //
                                        "{\\n" + //
                                        "  character(characterId: $characterId) {\\n" + //
                                        "    name\\n" + //
                                        "    gender\\n" + //
                                        "    status\\n" + //
                                        "    id\\n" + //
                                        "  }\\n" + //
                                        "  location(locationId: $locationId) {\\n" + //
                                        "    name\\n" + //
                                        "    dimension\\n" + //
                                        "  }\\n" + //
                                        "  episode(episodeId: 3360) {\\n" + //
                                        "    name\\n" + //
                                        "    air_date\\n" + //
                                        "    episode\\n" + //
                                        "  }\\n" + //
                                        "\\n" + //
                                        "}\\n" + //
                                        "\",\"variables\":{\"characterId\":7115,\"locationId\":11130}}\r\n" + //
                                        "")
            .when()
                .get("https://rahulshettyacademy.com/gq/graphql")
            .then()
                .log().all()
                .extract().response().asString();

                System.out.println(response);

                JsonPath responseJson = new JsonPath(response);
                String characterName = responseJson.getString("data.character.name");
                System.out.println(characterName);

    }
    @Test
    public void graphQLMutaationTest() {
        String characterNameValue = "Link";
        String response = given()
            .log().all()
            .header("Content-Type", "application/json")
            .body("{\"query\":\"mutation($locationName:String!,$characterName:String!,$episodeName:String!)\\n" + //
                                "{\\n" + //
                                "  createLocation(location: {\\n" + //
                                "    name: $locationName,\\n" + //
                                "    type: \\\"Nintendo\\\",\\n" + //
                                "    dimension: \\\"Isometric\\\"\\n" + //
                                "  }){\\n" + //
                                "\\tid\\n" + //
                                "  }\\n" + //
                                "  \\n" + //
                                "  createCharacter(character: {\\n" + //
                                "    name: $characterName,\\n" + //
                                "    type: \\\"Hero\\\",\\n" + //
                                "    status: \\\"Alive\\\",\\n" + //
                                "    species: \\\"Hylian\\\",\\n" + //
                                "    gender: \\\"Male\\\",\\n" + //
                                "    image: \\\"png\\\",\\n" + //
                                "    originId: 11123,\\n" + //
                                "    locationId: 11123    \\n" + //
                                "  }){\\n" + //
                                "    id\\n" + //
                                "  }\\n" + //
                                "  \\n" + //
                                "    createEpisode(episode:{\\n" + //
                                "    name: $episodeName,\\n" + //
                                "    air_date: \\\"07/02/2024\\\",\\n" + //
                                "    episode: \\\"1\\\"\\n" + //
                                "  }){\\n" + //
                                "    id\\n" + //
                                "  }\\n" + //
                                "}\",\"variables\":{\"characterId\":11124,\"locationId\":11123,\"episodeId\":8754,\"locationName\":\"Hyrule\",\"characterName\":\""+characterNameValue+"\",\"episodeName\":\"There's no way that this is that difficult!\"}}\r\n" + //
                                "")
        .when()
            .post("https://rahulshettyacademy.com/gq/graphql")
        .then()
            .extract().response().asString();

        System.out.println(response);

        JsonPath responseJson = new JsonPath(response);
        String characterId = responseJson.getString("data.createCharacter.id");
        System.out.println(characterId);

    }

}
