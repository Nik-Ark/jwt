#!/bin/bash
sshpass -f ../password/pass mysql -u root -p < ../sql/drop_user_and_db.sql