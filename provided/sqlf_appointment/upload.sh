#!/bin/bash

scp target/ttg-sqlf-1.0.0.jar server2.dev:ext-lib/
scp target/ttg-sqlf-1.0.0.jar server3.dev:ext-lib/
scp target/ttg-sqlf-1.0.0.jar server4.dev:ext-lib/
scp target/ttg-sqlf-1.0.0.jar server5.dev:ext-lib/

# scp src/main/conf/log4j.properties server2.dev:ext-lib/
# scp src/main/conf/log4j.properties server3.dev:ext-lib/
# scp src/main/conf/log4j.properties server4.dev:ext-lib/
# scp src/main/conf/log4j.properties server5.dev:ext-lib/

scp src/main/conf/logback.xml server2.dev:ext-lib/
scp src/main/conf/logback.xml server3.dev:ext-lib/
scp src/main/conf/logback.xml server4.dev:ext-lib/
scp src/main/conf/logback.xml server5.dev:ext-lib/

scp src/main/scripts/server.sh server2.dev:
scp src/main/scripts/server.sh server3.dev:
scp src/main/scripts/server.sh server4.dev:
scp src/main/scripts/server.sh server5.dev:
