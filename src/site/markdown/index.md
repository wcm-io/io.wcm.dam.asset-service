## About DAM Asset Service (deprecated)

A RESTful service for resolving URLs to assets and renditions.

[![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.dam.asset-service)](https://repo1.maven.org/maven2/io/wcm/io.wcm.dam.asset-service/)


### Documentation

* [REST Interface][rest-interface]
* [Configuration][configuration]
* [Changelog][changelog]


### Overview

The DAM Asset Services publishes a REST interface which allows to check if a DAM asset path is valid and replicated to a AEM instance. It resolves URLs to real or virtual renditions rendered on-the-fly. A data version is generated for each configured DAM asset folder to check if any asset was changed since last check.

Internally the [wcm.io Media Handler][media-handler] is used for building the URLs to the renditions. They can be requested by dimensions or media formats.

See [REST Interface][rest-interface] for a description of the interface.


### AEM Version Support Matrix

|DAM Asset Service version |AEM version supported
|--------------------------|----------------------
|1.5.x or higher           |AEM 6.5.7+, AEMaaCS
|1.4.x                     |AEM 6.4+, AEMaaCS
|1.3.x                     |AEM 6.1+
|1.0.x - 1.2.x             |AEM 6.0+


### Dependencies

To use this module you have to deploy also:

|---|---|---|
| [wcm.io Sling Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) |
| [wcm.io AEM Sling Models Extensions](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.models)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) |
| [wcm.io WCM Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.wcm.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) |
| [wcm.io Handler Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) |
| [wcm.io URL Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.url)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) |
| [wcm.io Media Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.media)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) |


### GitHub Repository

Sources: https://github.com/wcm-io/io.wcm.dam.asset-service


[rest-interface]: rest-interface.html
[configuration]: configuration.html
[changelog]: changes-report.html
[media-handler]: https://wcm.io/handler/media/
