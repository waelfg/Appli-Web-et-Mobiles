#!/bin/bash
echo "Removal of potential bad files"
sudo rm /tmp/mongodb-*
echo "Auto-repairing of mongo database..."
mongod --repair --config server/mongod.conf
echo "Starting mongod database server..."
echo ""
echo "Do not close this terminal or the process will be killed and database won't be accessible"
mongod --config server/mongod.conf
