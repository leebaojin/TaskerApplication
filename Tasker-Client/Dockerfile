FROM node:alpine

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
CMD ["npm", "start"]

