package components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private Team team;

    @BeforeEach
    public void setUp() {
        team = new Team("Test Team", "TT", 3, "#ed4848", "", true);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Team", team.getTeamName(), "Team name should be 'Test Team'");
        assertEquals(0, team.getTeamScore(), "Initial score should be 0");
        // assertNotNull(team.getLogo(), "Logo should not be null");
        assertNotNull(team.getPlayer(0), "Robot at index 0 should not be null");
        assertNotNull(team.getPlayer(1), "Robot at index 1 should not be null");
        assertNotNull(team.getPlayer(2), "Robot at index 2 should not be null");
        assertThrows(IndexOutOfBoundsException.class, () -> team.getPlayer(3), "Should throw exception for invalid index");
    }

    @Test
    public void testGetScore() {
        assertEquals(0, team.getTeamScore(), "Initial score should be 0");
    }

    @Test
    public void testSetScore() {
        team.setTeamScore(10);
        assertEquals(10, team.getTeamScore(), "Score should be 10");
    }

    @Test
    public void testGetTeamName() {
        assertEquals("Test Team", team.getTeamName(), "Team name should be 'Test Team'");
    }

    // @Test
    // public void testGetLogo() {
    //     assertNotNull(team.getLogo(), "Logo should not be null");
    // }

    @Test
    public void testGetPlayer() {
        assertNotNull(team.getPlayer(0), "Robot at index 0 should not be null");
        assertNotNull(team.getPlayer(1), "Robot at index 1 should not be null");
        assertNotNull(team.getPlayer(2), "Robot at index 2 should not be null");
    }

}
