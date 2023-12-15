FROM ultralytics/ultralytics:latest

RUN apt install wget
RUN wget https://github.com/ultralytics/assets/releases/download/v0.0.0/yolov8n.pt

COPY image-processor.py /

