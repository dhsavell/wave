<p align="center">
    <img src="./assets/wave-logo-text-2000.png" height="70px"/>
    <br/><br/>
    <a href="https://codeclimate.com/github/dhsavell/wave/maintainability"><img src="https://api.codeclimate.com/v1/badges/cef270fec0ece8f7bac8/maintainability" /></a>
</p>

# wave

> A Discord bot focused on server administration.

Wave is an advanced administration bot that is easily approachable yet very
powerful. There is currently no official public instance.

## Developing Wave

The included Gradle wrapper can be used to build, test, and run the bot. The
code is separated into two modules:

- `wave-app` - Contains features available on the official Wave instance.
- `wave-core` - Core bot framework.

The bot can also be run directly from Gradle for testing:

```sh
./gradlew wave-app:run --args='--token="<discord bot token>"'
```
