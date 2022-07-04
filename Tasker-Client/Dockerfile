FROM node:alpine

# Setting up ARG variables for the url
ARG epURL=http://152.67.99.60:8085
ENV epURL_env=$epURL

# Create new app directory (at the image side)
Run mkdir /app

# Copy the pakage.json
COPY /package.json /app
COPY ./ /app

# The app directory
WORKDIR /app

# Install the necessary files
RUN npm install

# To select the port
EXPOSE 3000

# Default executable command

CMD REACT_APP_APIURL=${epURL_env} npm start

# CMD["npm","start"]