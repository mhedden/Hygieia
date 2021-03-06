## Hygieia UI

### Requirements

- NodeJS
- npm
- gulp
- bower

#### Mac OS X

    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    brew install node
    npm install -g bower
    npm install -g gulp

Pull down everything that's configured with bower and npm. I think it's:

    npm install
    bower install

Will need up update the ngFitText bower.json file to point to 'src/ng-FitText.js' instead of '/src/ng-FitText.js'

#### Windows

Install NodeJS using the MSI package available at: http://nodejs.org/download/

Issue the following commands via command line:

	npm install -g bower
	npm install -g gulp

Navigate to your project root via command line and use the following command:

	npm install

Use Git Shell to install bower in the following manner; do so from your project's root directory:

	bower install
	select option 2 when prompted for user input

Run the dashboard from the following command:

	gulp serve



### Layouts
Are under src/components/templates. Currently only capone is used. Just add ```<widget name="[your new widget name]"></widget>``` and you're good to go.
All widgets have to be hardcoded into the layout right now.


### Running
In terminal navigate to the project root and run ```gulp serve```. Should serve up on port 3000.  

Local Testing with Mocks:

```bash
 gulp serve --local true
```

or you can run via maven from UI project root folder
 ```bash
 mvn clean package integration-test
 ```
 for local testing of Hygieia UI layer

All data is currently coming from the test-data folder so you shouldn't need an api, but also means no settings will be saved..


### Docker

#### Create

```bash
# from top-level project
mvn clean package -pl UI docker:build
```

#### Run

```bash
docker run -t -p 8088:80 --link hygieia-api -i hygieia-ui:latest
```
