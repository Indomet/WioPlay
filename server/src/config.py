import os
from dotenv import load_dotenv

load_dotenv()


class API_KEYS:
    youtube: str = os.getenv('API_KEY')
