# STAGE 1: Build the React App
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
# This compiles your React code into tiny, optimized static files
RUN npm run build

# STAGE 2: Serve with Nginx
FROM nginx:alpine
# Copy the compiled files from Stage 1 into the Nginx server directory
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]