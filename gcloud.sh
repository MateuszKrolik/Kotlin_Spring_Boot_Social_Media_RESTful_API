#! /bin/bash

source .env

gcloud projects add-iam-policy-binding $GCLOUD_ID \
  --member=serviceAccount:$GCLOUD_PROJECT_NUMBER@cloudbuild.gserviceaccount.com \
  --role=roles/artifactregistry.writer

gcloud projects add-iam-policy-binding $GCLOUD_ID \
  --member=serviceAccount:$GCLOUD_PROJECT_NUMBER@cloudbuild.gserviceaccount.com \
  --role=roles/run.admin

gcloud projects add-iam-policy-binding $GCLOUD_ID \
  --member=serviceAccount:$GCLOUD_PROJECT_NUMBER@cloudbuild.gserviceaccount.com \
  --role=roles/iam.serviceAccountUser

gcloud projects add-iam-policy-binding $GCLOUD_ID \
  --member=serviceAccount:$GCLOUD_PROJECT_NUMBER@cloudbuild.gserviceaccount.com \
  --role=roles/secretmanager.secretAccessor

gcloud projects add-iam-policy-binding $GCLOUD_ID \
  --member=serviceAccount:$GCLOUD_PROJECT_NUMBER@cloudbuild.gserviceaccount.com \
  --role=roles/cloudbuild.builds.editor

