# SpamGuardBot

A Telegram bot that uses machine learning to detect and remove spam messages in real time. It helps group admins keep chats clean by automatically filtering unwanted ads and spam. The bot analyzes each message, calculates spam probability, and decides whether to delete or keep it, improving chat quality.

## Demo Video

[![Watch the project in action](https://img.youtube.com/vi/kxPaYbDYAjU/0.jpg)](https://youtu.be/kxPaYbDYAjU)

[//]: # (## Installation Guide)

[//]: # ()

[//]: # (Follow our simple [installation guide]&#40;https://google.com/&#41; to set up the bot.)

## Testing & Results

> This repository contains test cases for the spam detection model. The model is trained using standard machine learning methods. For details and the model itself, see the [model repository](https://github.com/IlliaFransua/SpamGuardService).

Below are sample messages with spam probability and the bot’s decision:

<table>
  <thead>
    <tr>
      <th style="padding: 8px; border: 1px solid #ccc;">Input Data</th>
      <th style="padding: 8px; border: 1px solid #ccc;">Spam Chance</th>
      <th style="padding: 8px; border: 1px solid #ccc;">Decision</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Congratulations! You've won a free iPhone! Click here to claim your prize!</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">72.25%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Unique offer! Earn $1000 a day from home! Find out more!</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffcc99;">61.17</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Urgent! Your account is locked. Click the link to unlock it.</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">72.81%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hi! How are you? It's been a while since we last met; let's grab a coffee.</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">52.05%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">I found a cool article about new technology that I think you'll like.</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">50.00%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Don't forget our meeting on Friday at 3 PM. Can't wait to see you!</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">50.02%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hey! I found a good way to help you with your money, and I want to share it with you.</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffcc99;">64.24%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hi! I have a link to a free eBook about money tips that I think you’ll find useful. Want it?</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffcc99;">68.27%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hey! I saw a webinar about smart investing. It might help you!</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffcc99;">69.06%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hi! How are you? I just got back from a trip and want to tell you about it.</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">51.53%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hello! I found a project that could help our team. Can we talk about it?</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">51.02%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">Hey! I have a few ideas for our next event. When would be a good time for us to meet?</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">51.36%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
<tr>
      <td style="padding: 8px; border: 1px solid #ccc;">5 year later still love song br axy665</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">59.13%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">wanna earn money online without investmentjust visit link somelink.com</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">73.03%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">glad know im one know katheryns birthday today happy birthday katyand sister seriously birthday</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">50.00%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">hi wont b ard 4 christmas enjoy n merry xmas</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">57.18%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">im fine hope also</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffff99;">50.14%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ccffcc;">Keep</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">watchvdtqcftr1fac justien bieber car 2013 likeampsubscribe</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ffcc99;">66.65%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">hey check new website site newwebsite.com</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">71.51%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
    <tr>
      <td style="padding: 8px; border: 1px solid #ccc;">subscribe win capbr</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">70.45%</td>
      <td style="padding: 8px; border: 1px solid #ccc; background-color: #ff9999;">Delete</td>
    </tr>
  </tbody>
</table>
