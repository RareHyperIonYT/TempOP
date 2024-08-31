## Introduction

**Temporary OP** is a free and simple Minecraft plugin that allows server administrators to temporarily grant operator (OP) status to players. This plugin helps maintain server security by automatically revoking temporary OP privileges after a specified duration.

## Features
- **Temporary OP:** Grant the operator status to players for a limited time using easy commands.
- **Persistence Across Restart:** Automatically saves the list of temporarily OP'd players to ensure they retain their status after server restarts or shutdown.
- **Restricted Permissions:** Prevents temporarily OP'd players from granting, revoking, or reassigning OP status to others, closing potential loopholes in server management.

## Commands
- `/tempop <player> [10s / 1d / 1mo]`: Temporarily grants OP status to the specified player for a defined duration. If no duration is specified, the plugin uses the default duration configured in `config.yml`.
    - `<player>`: The username of the player to be granted OP.
    - `[10s / 1d / 1mo]`: (Optional) The duration for which OP is granted. Accepts formats like seconds (`s`), minutes (`m`), hours (`h`), days (`d`), weeks (`w`), and months (`mo`).

## Permissions
To manage the use of the `/tempop` command, assign the following permission nodes:
- `tempop.use`: Allows the player to use the `/tempop` command to grant temporary operator status to others.

## Notes
- The plugin does not inerfere with permanent OP assignments made through the servers standard OP command.

## Configuration
<details>
  <summary>config.yml</summary>

  ```yml
  # The default duration for temporarily granting operator status if no duration is specified.
  default-duration: "5m"

  # Whether the console is allowed to temporarily grant operator status to players.
  console-allowed: true

  message:
    # Displayed when the command is attempted from the console with "console-allowed" set to false.
    console: "&cYou must be a player to use this command."

    # Message shown when a player tries to run the command without the necessary permissions to do so.
    permission: "&cYou do not have permission to run this command."

    # Message sent when a temporary operator attempts to grant operator status to others.
    op: "&cTemporary operators aren't permitted to op others."

    # Message sent when a temporary operator attempts to remove operator status from others.
    deop: "&cTemporary operators aren't permitted to deop others."

    # Displayed when a player specified in the command cannot be found.
    player: "&cThere is no player called \"%s\"."

    # Confirmation message sent to the player when successfully granting temporary operator status.
    give: "&aSuccessfully gave {player} operator temporarily for {duration}."

    # Displayed when a player is granted temporary operator status.
    given: "&aYou were temporarily given operator for {duration} by {player}."

    # Notification sent to a player when their temporary operator status expires.
    expire: "&cYour OP status has expired."
  ```

</details>

## Support
For any issues, questions, or suggestions, please create an issue on GitHub.