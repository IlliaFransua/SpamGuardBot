import os
import sys

import structlog
from aiogram import Router, F, Bot
from aiogram.exceptions import TelegramBadRequest
from aiogram.filters import Command, CommandStart, CommandObject
from aiogram.types import Message, ContentType, LabeledPrice, PreCheckoutQuery, InlineKeyboardMarkup
from aiogram.utils.keyboard import InlineKeyboardBuilder

from fluent.runtime import FluentLocalization
from PIL import Image
import numpy as np
import io

from submodules.SpamGuardService.spam_detection_model import SpamDetectionModel

# Declare spam detection model
spam_detector = SpamDetectionModel()

# Declare router
router = Router()
router.message.filter(F.chat.type.in_({"group", "supergroup"}))

# Declare logger
logger = structlog.get_logger()


# Handlers:
@router.message(F.content_type.in_({'new_chat_members', 'left_chat_member'}))
async def on_user_join_or_left(message: Message):
    """
    Removes "user joined" and "user left" messages.
    By the way, bots do not receive left_chat_member updates when the group has more than 50 members (otherwise use https://core.telegram.org/bots/api#chatmemberupdated)
    :param message: Service message "User joined group
    """

    await message.delete()


@router.message(Command("report"))
async def report_spam(message: Message):
    if not message.reply_to_message or not message.reply_to_message.text:
        await message.reply("Команду нужно использовать в ответ на текстовое сообщение.")
        return

    spam_text = message.reply_to_message.text

    spam_detector.train([spam_text], [])

    await message.reply("Сообщение помечено как спам и добавлено в базу.")
    logger.info(f"Добавлен новый спам: {spam_text}")


@router.message(F.content_type == ContentType.TEXT)
async def on_text_message(message: Message):
    """
    :param message: The message object containing the text.
    """
    logger.info(f"Received text message: {message.text}")

    if message.text == "яблочки":
        # await message.delete()
        await message.reply("Бан ворд")
        return

    if spam_detector.predict(message.text):
        # await message.delete()
        await message.reply("Спам, -10 очков Грифиндору")
        return

    await message.reply("А вот это не спам, +10 очков Когтеврану")


@router.message(F.content_type == ContentType.PHOTO)
async def on_photo_message(message: Message):
    """
    Handler for photo messages.
    Downloads the photo and processes it as a 2D array of pixels.
    :param message: The message object containing the photo.
    """
    logger.info(f"Received photo message from {message.from_user.id}")

    photo = message.photo[-1]
    file_info = await message.bot.get_file(photo.file_id)

    file_path = file_info.file_path
    file = await message.bot.download_file(file_path)

    image_stream = io.BytesIO(file.getvalue())

    image = Image.open(image_stream)
    image = image.convert("RGB")
    pixel_array = np.array(image)

    logger.info(f"shape: {pixel_array.shape}")

    await message.reply(f"shape: {pixel_array.shape}")
