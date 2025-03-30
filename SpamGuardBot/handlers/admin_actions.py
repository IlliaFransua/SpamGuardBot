import structlog
from aiogram import Router, F, Bot
from aiogram.filters import Command, CommandStart, CommandObject
from aiogram.types import Message, InlineKeyboardMarkup
from aiogram.utils.keyboard import InlineKeyboardBuilder

from fluent.runtime import FluentLocalization

from filters.is_owner import IsOwnerFilter

# Declare router
router = Router()
router.message.filter(F.chat.type == "private", IsOwnerFilter(is_owner=True))

# Declare handlers
logger = structlog.get_logger()

# Handlers:
@router.message(Command("start"))
async def cmd_owner_hello(message: Message, l10n: FluentLocalization):
    await message.answer(
        l10n.format_value("hello-owner")
    )

@router.message(Command("ping"))
async def cmd_ping_bot(message: Message, l10n: FluentLocalization):
    await message.reply(l10n.format_value("ping-msg"))

@router.message(
    IsOwnerFilter(is_owner=True),
    Command(commands=["filter_text_on"]),
)
async def cmd_filter_text_on(message: Message, l10n: FluentLocalization):
    await message.reply(l10n.format_value("filter-text-on-msg"))

@router.message(
    IsOwnerFilter(is_owner=True),
    Command(commands=["filter_text_off"]),
)
async def cmd_filter_text_off(message: Message, l10n: FluentLocalization):
    await message.reply(l10n.format_value("filter-text-off-msg"))

@router.message(
    IsOwnerFilter(is_owner=True),
    Command(commands=["filter_image_on"]),
)
async def cmd_filter_image_on(message: Message, l10n: FluentLocalization):
    await message.reply(l10n.format_value("filter-image-on-msg"))

@router.message(
    IsOwnerFilter(is_owner=True),
    Command(commands=["filter_image_off"]),
)
async def cmd_filter_image_off(message: Message, l10n: FluentLocalization):
    await message.reply(l10n.format_value("filter-image-off-msg"))
