# ConstVidSearch

**ConstVidSearch** is an advanced Android application that provides constant-time video search using a vector-based indexing method. This project demonstrates how modern search mechanisms can be implemented efficiently for scalable video datasets.

## Features

- **Constant-Time Search**: Implements O(1) complexity for retrieving relevant videos based on vector embeddings.
- **Transformer-Based Embeddings**: Uses Microsoft multilingual transformers to generate vector embeddings for video titles and descriptions.
- **Cloud Storage**: Video files and metadata are stored securely on AWS services like S3 and DynamoDB.
- **Scalable Architecture**: Designed to handle large-scale video datasets using Pinecone Vector Database for fast and accurate retrieval.
- **User-Friendly Interface**: Allows users to search videos by title and view results instantly.

## Architecture Overview

### Preprocessing Phase
1. **User Input**: Users upload a video with a title, description, and date.
2. **Storage**:
   - The video, thumbnail, and metadata are stored in an AWS S3 Bucket.
   - Metadata (e.g., title, description, thumbnail URL, video URL) is stored in DynamoDB.
3. **Embedding Generation**:
   - Titles and descriptions are transformed into vector embeddings using Microsoft multilingual transformers.
   - The embeddings are indexed into Pinecone Vector Database for fast retrieval.

### Searching Phase
1. **User Query**: The user searches for a video by providing a title.
2. **Vector Matching**:
   - The query title is transformed into a vector using the same transformer model.
   - Pinecone Vector Database performs a nearest neighbor search to retrieve top K matching results.
3. **Data Retrieval**:
   - Metadata of the top results is fetched from DynamoDB using partition keys.
   - Results (title, description, thumbnail, video URL) are displayed on the user interface.

## Tech Stack

- **Android**: Java-based mobile application development.
- **AWS Services**: S3 (video storage), DynamoDB (metadata storage).
- **Transformer Models**: Microsoft multilingual transformer.
- **Vector Database**: Pinecone for fast and scalable vector search.

## How to Use

1. **Upload a Video**:
   - Enter the video title, description, and date.
   - Upload the video file and thumbnail.
2. **Search a Video**:
   - Use the search bar to enter a video title.
   - View the search results with relevant video thumbnails and descriptions.

## Screenshots

### Preprocessing Flow
![Preprocessing Architecture](https://github.com/user-attachments/assets/8c1ee7b0-3a34-402c-a3df-c06c5748388c
)

### Searching Flow
![Searching Architecture](https://github.com/user-attachments/assets/70f4d654-a21f-4350-857a-52a479ac74db
)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ConstVidSearch.git
   ```
2. Open the project in Android Studio.
3. Configure your AWS credentials in the app settings.
4. Build and run the application on an Android device/emulator.

## Future Enhancements

- Add support for advanced filters like tags, categories, and timestamps.
- Implement personalized recommendations based on user history.
- Support multilingual search queries.

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

- **GeeksforGeeks Profile**: [Your gfg](https://www.geeksforgeeks.org/user/garimamillicent/)
- **LinkedIn**: [Your LinkedIn](https://www.linkedin.com/in/garima-kashyap-75b1202b8/)
- **GitHub**: [Your GitHub](https://github.com/garimaakashyap)

---

Feel free to explore, contribute, and give feedback on the project. Together, let's make video search faster and smarter! 🚀
